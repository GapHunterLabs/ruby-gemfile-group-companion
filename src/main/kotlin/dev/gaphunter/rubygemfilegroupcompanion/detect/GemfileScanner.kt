package dev.gaphunter.rubygemfilegroupcompanion.detect

import dev.gaphunter.rubygemfilegroupcompanion.model.GemHit

/**
 * Plain-text line scanner for a `Gemfile` -- flags a well-known
 * dev/test gem ([KnownDevTestGems]) declared at the top level (not
 * inside any `group :development`/`group :test`/`group :dev, :test`
 * block). A dev/test gem left ungrouped ships in every environment,
 * including production, bloating the bundle and (for something like
 * `pry`/`letter_opener`) a real footgun if it's ever accidentally
 * usable in production.
 *
 * **Deliberately line-based, not a real Ruby parser** -- same
 * discipline as `ConfigLineScanner`. Tracks `group ... do` / `end`
 * nesting depth via a simple counter: once inside a `group` block (any
 * group, not just dev/test -- a gem inside `group :production do` is
 * still "grouped", just not a group this plugin flags), a gem
 * declaration there is never flagged, regardless of which group it's
 * in.
 *
 * **v0.1 scope, stated honestly:** only the common
 * `group :development, :test do ... end` block form is recognized --
 * the single-line `gem "x", group: :test` form (an inline `group:`
 * keyword argument) is a real, valid alternative Bundler syntax that
 * isn't specially handled in v0.1.
 */
object GemfileScanner {

    private val GEM_LINE = Regex("""^gem\s+["']([\w.-]+)["']""")
    private val GROUP_BLOCK_START = Regex("""^group\s+.*\bdo\b\s*$""")
    private val BLOCK_END = Regex("""^end\s*$""")
    // Any other line ending in a bare `do` opens a nested block this scanner must also track,
    // so `end` accounting stays correct even for blocks unrelated to `group`.
    private val OTHER_BLOCK_START = Regex(""".*\bdo\b(\s*\|[^|]*\|)?\s*$""")

    fun scan(text: String): List<GemHit> {
        val hits = mutableListOf<GemHit>()
        var groupDepth = 0 // > 0 means currently inside a `group ... do` block (at any nesting level)
        var otherBlockDepth = 0 // tracks non-group `do`/`end` blocks opened while groupDepth == 0

        text.lines().forEachIndexed { index, rawLine ->
            val trimmed = rawLine.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#")) return@forEachIndexed

            when {
                GROUP_BLOCK_START.matches(trimmed) -> {
                    groupDepth++
                    return@forEachIndexed
                }
                groupDepth > 0 && OTHER_BLOCK_START.matches(trimmed) -> {
                    groupDepth++ // nested block inside a group -- still "inside a group" until it closes
                    return@forEachIndexed
                }
                groupDepth > 0 && BLOCK_END.matches(trimmed) -> {
                    groupDepth--
                    return@forEachIndexed
                }
                groupDepth == 0 && OTHER_BLOCK_START.matches(trimmed) -> {
                    otherBlockDepth++
                    return@forEachIndexed
                }
                groupDepth == 0 && otherBlockDepth > 0 && BLOCK_END.matches(trimmed) -> {
                    otherBlockDepth--
                    return@forEachIndexed
                }
            }

            if (groupDepth > 0) return@forEachIndexed

            val match = GEM_LINE.find(trimmed) ?: return@forEachIndexed
            val gemName = match.groupValues[1]
            if (gemName in KnownDevTestGems.NAMES) {
                hits += GemHit(gemName, index + 1)
            }
        }

        return hits
    }
}

package dev.gaphunter.rubygemfilegroupcompanion.model

/** One well-known dev/test gem declared outside any `group :development`/`group :test` block. */
data class GemHit(val gemName: String, val lineNumber: Int)

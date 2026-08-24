package dev.gaphunter.rubygemfilegroupcompanion.detect

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GemfileScannerTest {

    @Test
    fun `an ungrouped dev gem is flagged`() {
        val text = """
            source "https://rubygems.org"
            gem "rails"
            gem "rspec"
        """.trimIndent()
        val hits = GemfileScanner.scan(text)
        assertEquals(1, hits.size)
        assertEquals("rspec", hits[0].gemName)
    }

    @Test
    fun `a gem inside a group block is not flagged`() {
        val text = """
            source "https://rubygems.org"
            gem "rails"

            group :development, :test do
              gem "rspec"
              gem "pry"
            end
        """.trimIndent()
        assertTrue(GemfileScanner.scan(text).isEmpty())
    }

    @Test
    fun `a non-dev-test gem is never flagged even ungrouped`() {
        val text = """
            gem "rails"
            gem "pg"
        """.trimIndent()
        assertTrue(GemfileScanner.scan(text).isEmpty())
    }

    @Test
    fun `a gem inside an unrelated do block is not miscounted as grouped`() {
        val text = """
            if RUBY_VERSION >= "3.0"
              gem "some_conditional_gem"
            end
            gem "rspec"
        """.trimIndent()
        val hits = GemfileScanner.scan(text)
        assertEquals(1, hits.size)
        assertEquals("rspec", hits[0].gemName)
    }

    @Test
    fun `nested blocks inside a group still count as grouped`() {
        val text = """
            group :test do
              platforms :ruby do
                gem "rspec"
              end
            end
        """.trimIndent()
        assertTrue(GemfileScanner.scan(text).isEmpty())
    }

    @Test
    fun `a gem after a group block closes is checked again at top level`() {
        val text = """
            group :test do
              gem "capybara"
            end
            gem "rubocop"
        """.trimIndent()
        val hits = GemfileScanner.scan(text)
        assertEquals(1, hits.size)
        assertEquals("rubocop", hits[0].gemName)
    }
}

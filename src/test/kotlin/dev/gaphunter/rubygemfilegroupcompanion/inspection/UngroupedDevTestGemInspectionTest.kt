package dev.gaphunter.rubygemfilegroupcompanion.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class UngroupedDevTestGemInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(UngroupedDevTestGemInspection::class.java)
    }

    fun `test an ungrouped dev gem produces a warning`() {
        myFixture.configureByText(
            "Gemfile",
            """
            source "https://rubygems.org"
            gem "rspec"
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("rspec") == true })
    }

    fun `test a grouped dev gem produces no warning`() {
        myFixture.configureByText(
            "Gemfile",
            """
            group :test do
              gem "rspec"
            end
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("rspec") == true })
    }

    fun `test a non-Gemfile file is never scanned`() {
        myFixture.configureByText(
            "notes.rb",
            "gem \"rspec\"",
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("rspec") == true })
    }
}

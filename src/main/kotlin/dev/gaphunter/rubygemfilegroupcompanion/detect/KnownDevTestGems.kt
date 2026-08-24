package dev.gaphunter.rubygemfilegroupcompanion.detect

/**
 * Well-known Ruby gems that exist specifically to support development
 * or testing -- never meant to ship in a production bundle. A curated
 * list, not an exhaustive one: only gems whose entire purpose is
 * dev/test tooling (a testing framework, a REPL, a linter, a test
 * double/fixture library) are included, deliberately excluding
 * anything that could plausibly be a real runtime dependency for some
 * projects.
 */
object KnownDevTestGems {

    val NAMES: Set<String> = setOf(
        "rspec",
        "rspec-rails",
        "minitest",
        "rubocop",
        "rubocop-rails",
        "rubocop-rspec",
        "pry",
        "pry-byebug",
        "byebug",
        "factory_bot",
        "factory_bot_rails",
        "capybara",
        "selenium-webdriver",
        "simplecov",
        "webmock",
        "vcr",
        "faker",
        "shoulda-matchers",
        "database_cleaner",
        "guard",
        "guard-rspec",
        "rerun",
        "spring",
        "brakeman",
        "bullet",
        "letter_opener",
        "annotate",
    )
}

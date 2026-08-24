# Ruby Gemfile Group Companion

Warning on a well-known dev/test gem (RSpec, RuboCop, Pry, FactoryBot,
Capybara, SimpleCov, WebMock, VCR, and similar) declared at a
`Gemfile`'s top level instead of inside a `group
:development`/`group :test` block — it ships in every environment,
including production, bloating the bundle and (for something like
Pry or letter_opener) a real footgun if it's ever accidentally
reachable in production.

## Why it exists

A gem added quickly during a debugging session, or copy-pasted above
the `group` blocks instead of inside them, is easy to miss on review —
`bundle install` never complains, it just silently ships everywhere.
Nothing in the IDE flags it today.

## Why built this way

- **100% static text analysis** — a plain-text line scanner, not a
  Ruby-language parser, so it works whether the real Bundler/RubyMine
  plugin is installed or not.
- **A curated, narrow gem list** — only gems whose entire purpose is
  dev/test tooling are included, deliberately excluding anything that
  could plausibly be a real runtime dependency for some project.

## v0.1 scope — stated honestly, not exhaustively

Only the common `group :development, :test do ... end` block form is
recognized — the single-line `gem "x", group: :test` inline form (a
valid alternative Bundler syntax) isn't specially handled.

## Usage

Open any `Gemfile`. A dev/test gem declared outside a `group` block
shows a warning.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.

# Demo data for screenshots

`Gemfile` — `rspec` is ungrouped (flagged), `pry`/`factory_bot_rails`
are correctly inside the `group :development, :test` block (not
flagged).

## How to get the screenshot

1. `./gradlew runIde` from `ruby-gemfile-group-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `Gemfile` — a warning should appear on the
   `gem "rspec"` line only.
3. Screenshot with the whole file visible, save into
   `ruby-gemfile-group-companion/docs/screenshots/`. Close the
   sandbox.

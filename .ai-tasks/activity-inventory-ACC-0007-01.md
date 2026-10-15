# ACC-0007-01 Medium-Priority Activity

Governed by: `@ACC-0007-01`

Feature file:

```text
full-application-acceptance/src/test/resources/com/evolutionnext/feature/activityinventory/activity-inventory.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/feature/activityinventory
```

Business behavior summary:

A logged-in user can add an activity with medium priority from the activity
inventory page. The saved activity is shown with its name and medium priority.
This scenario-level revision extends the priorities supported by ACC-0007; it
does not change activity persistence, Todo Today selection, or completion.

## Business Language And Specification

- [x] Add the scenario-level `@ACC-0007-01` revision and medium-priority
  example to the existing Activity Inventory feature.
- [x] Add `medium priority` to `.memory-bank/ubiquitous-language.md` as the
  priority used for an activity that should receive normal attention.
- [x] Keep the existing high-priority scenarios and `@ACC-0007` behavior
  unchanged.

## Domain Aggregate

- [x] Add `MEDIUM` to `ActivityPriority` so the existing `ActivityInventory`
  aggregate can retain a medium-priority activity without changing its add
  operation.
- [x] Add an example-based aggregate test that adds "Prepare Workshop Slides"
  with medium priority and reads back the same name and priority.
- [x] Add a priority arbitrary covering `HIGH` and `MEDIUM`, and extend the
  jqwik aggregate property to prove every valid generated priority is retained.

## Domain Service

- [x] Record that no domain service is needed because assigning a supported
  priority still changes only the `ActivityInventory` aggregate and introduces
  no cross-aggregate rule.

## Application Service

- [x] Reuse the existing add-activity command, result, command service, query
  service, and ports because they already carry `ActivityPriority` without
  high-priority-specific branching.
- [x] Add an example-based application service test proving a medium-priority
  command is returned through the query port with the same priority.
- [x] Extend the jqwik service property to generate every supported priority
  and prove each accepted command is observable through the query port.

## Repository

- [x] Reuse the existing repository port, in-memory adapter, JDBC adapter, and
  `activity_inventory.priority` text column because they persist enum names
  without a high-priority-only schema constraint.
- [x] Extend the in-memory repository jqwik property to generate and reload
  activities with every supported priority.
- [x] Extend the PostgreSQL repository property test with jqwik,
  jqwik-testcontainers, and Testcontainers to persist and reload every
  supported generated priority through a fresh JDBC query.
- [x] Leave `full-application-e2e/init.sql` unchanged after verifying its text
  priority column accepts `MEDIUM`.

## Domain Event

- [x] Record that no domain event is needed because ACC-0007-01 changes only
  the priority accepted and displayed inside the activity inventory feature.

## Controller

- [x] Add a "Medium priority" option with value `MEDIUM` to the activity
  inventory form.
- [x] Reuse the existing activity inventory handler conversion and title-case
  rendering so a submitted `MEDIUM` value reaches the command port and appears
  as "Medium priority".
- [x] Add an example-based HTTP handler test that submits a medium-priority
  activity and verifies a later activity inventory response shows its name and
  "Medium priority".
- [x] Extend the jqwik controller property to generate supported priorities
  and prove every successful POST is observable through GET with the matching
  user-facing priority text.
- [x] Run the existing Hexagonal ArchUnit rules; no boundary change is planned.

## E2E Testing

- [x] Add medium-priority Cucumber step definitions under the existing
  activity inventory acceptance package, reusing the scenario-scoped state and
  current Cucumber configuration.
- [x] Run the `@ACC-0007-01` scenario and keep the original `@ACC-0007`
  scenarios passing.
- [x] Add packaged REST Assured coverage that registers a user, posts
  "Prepare Workshop Slides" with `MEDIUM`, and verifies the response displays
  the activity with "Medium priority".
- [x] Update `ActivityInventoryPage` with medium-priority form interaction,
  keeping selectors, selection, submission, and waiting inside the Page Object.
- [x] Add a Selenium E2E test whose user flow and assertions verify that
  "Prepare Workshop Slides" is added and displayed with "Medium priority".
- [x] Reuse `ActivityComponent` to read the displayed name and priority because
  its interface is priority-neutral.
- [x] Reuse the existing Jib image build, filtered `.env`, Docker Compose,
  PostgreSQL, and Testcontainers support; no wiring or schema change is needed.
- [x] Run the Page Object ArchUnit test to confirm UI selectors and interactions
  remain outside the Selenium test.

## Validation

- [x] Run the focused `@ACC-0007-01` Cucumber scenario and the complete activity
  inventory Cucumber feature.
- [x] Run the activity inventory aggregate, application service, in-memory and
  PostgreSQL repository, and HTTP handler tests through Maven.
- [x] Run the focused Hexagonal and Page Object ArchUnit tests.
- [x] Build the full application image with Jib enabled for packaged E2E
  verification.
- [x] Run the REST Assured and Selenium activity inventory E2E tests through
  the existing Testcontainers-managed Docker Compose environment.
- [x] Run `sdk env` and the full `mvn verify` suite.
- [x] Record any environment-blocked validation gap here before marking the
  slice complete.


## Validation Notes

- Focused domain, application service, in-memory repository, PostgreSQL
  repository, HTTP handler, and Hexagonal architecture tests passed.
- The focused `@ACC-0007-01` Cucumber scenario passed; the full reactor ran all
  three Activity Inventory scenarios with no failures.
- The Jib application image built successfully with `-Djib.skip=false`.
- An initial focused packaged E2E run passed the new medium-priority API and UI
  flow but hit a Safari navigation timeout in the pre-existing high-priority
  leave-and-return flow.
- The subsequent full `mvn verify` reran both Activity Inventory REST Assured
  tests and both Selenium tests successfully, along with the Page Object and
  Hexagonal architecture tests.
- Final validation has no outstanding gap.

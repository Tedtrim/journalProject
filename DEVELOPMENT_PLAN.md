# Journal App — Development Plan

## Current State

The project has a working skeleton: a Maven/JavaFX 21 setup, a basic `JournalEntry` data model, an in-memory `JournalStore` (HashMap-backed), and a minimal UI with a text area and a save button. The critical gap is **no persistence** — all entries vanish on restart — and the UI only supports writing, not reading, editing, or deleting entries.

---

## Phase 1 — Fix the Foundation (Do This First)

Before adding new features, a few structural issues need resolving.

**1.1 Fix the `pom.xml` main class reference**
The pom.xml points to `org.example.javafx.HelloApplication`, but the real entry point is `UI.JournalUI`. Running via Maven (`mvn javafx:run`) will fail until this is corrected.

```xml
<!-- In pom.xml, update the javafx-maven-plugin configuration -->
<mainClass>UI.JournalUI</mainClass>
```

**1.2 Rename packages to follow Java conventions**
`UI` and `data` should be lowercase and under a proper root package. Refactor to something like `com.journal.ui` and `com.journal.data`. IntelliJ can do this automatically via Refactor → Move.

**1.3 Delete the unused FXML file**
`hello-view.fxml` is a leftover from the JavaFX template. Remove it to keep the project clean, or commit to using FXML for the UI (see Phase 2).

---

## Phase 2 — Persistence (The Most Important Feature)

Without saving data to disk, the app is a toy. Add file-based persistence using JSON — it's simple, human-readable, and requires no database setup.

**Recommended approach: JSON via Gson or Jackson**

Add the dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

**2.1 Create a `FileStore` class**
Write a `FileStore` (implementing the same interface as `JournalStore`) that serializes entries to a JSON file in the user's home directory, e.g. `~/.journal/entries.json`. Load entries on startup, save on every write.

```java
// Target file location
Path journalFile = Path.of(System.getProperty("user.home"), ".journal", "entries.json");
```

**2.2 Design the storage format**
Each entry maps a date string to entry text:
```json
{
  "2026-04-24": "Today I worked on my journal app.",
  "2026-04-23": "Started the JavaFX project."
}
```

**2.3 Extract a `JournalRepository` interface**
Define an interface so `JournalStore` (in-memory, for tests) and `FileStore` (for production) are interchangeable. Wire `FileStore` into `JournalUI` for the real app.

---

## Phase 3 — Expand the UI

Once data persists, users need to actually see and manage their entries. This is where the app goes from functional to useful.

**3.1 Add an entry list panel**
Use a `ListView<LocalDate>` on the left side of the window. Populate it from the store on startup and refresh after every save. Clicking a date loads that entry into the text area.

**3.2 Support editing and deleting**
- Add an "Edit" mode: clicking a past entry loads it into the text area; saving overwrites it.
- Add a "Delete" button (with a confirmation dialog) that calls `JournalStore.delete()`.

**3.3 Add a date picker for new entries**
Currently entries are always stamped with today's date. A `DatePicker` control lets users backfill missed days.

**3.4 Resize the window**
300×200 is very small. Expand to at least 700×500 and use a `SplitPane` to divide the entry list from the editor.

**3.5 Choose: programmatic UI or FXML**
The project already has an FXML file. Committing to FXML + Scene Builder makes the UI easier to design visually and separates layout from logic (true MVC). Alternatively, keep building programmatically — either is fine, but pick one and be consistent.

---

## Phase 4 — Architecture & Code Quality

As the app grows, keeping it tidy will save time.

**4.1 Adopt MVC properly**

| Layer | Class | Responsibility |
|---|---|---|
| Model | `JournalEntry`, `JournalRepository` | Data and business logic |
| View | FXML files | Layout and styling |
| Controller | `JournalController` | Handles UI events, talks to the model |

Move all event handling out of `JournalUI` and into a dedicated controller class.

**4.2 Write unit tests**
JUnit 5 is already in the dependencies — use it. Priority tests:

- `JournalStore`: save, get, edit, delete, duplicate date behaviour
- `FileStore`: round-trip serialization (write then read back)
- `JournalEntry`: immutability of date, setter behaviour

**4.3 Add input validation and error handling**
- Warn the user if they try to save over an existing entry without confirming
- Show a meaningful error dialog if the file can't be written (e.g. permissions)
- Guard against null/empty text everywhere

---

## Phase 5 — Polish and Extra Features

Once the core is solid, these features would make the app genuinely pleasant to use.

**5.1 Search**
A search bar that filters the entry list by keyword. Can be done in-memory over loaded entries.

**5.2 Basic CSS styling**
JavaFX supports CSS stylesheets. A `.css` file can give the app a clean, readable look — custom fonts, background colours, button styles.

**5.3 Word/character count**
Display a live character count below the text area using a `ChangeListener` on the text property.

**5.4 Export to plain text or PDF**
Let users export a date range of entries as a `.txt` file. Useful for printing or backing up.

**5.5 Tags or moods**
Optional metadata on each entry (e.g. `#productive`, `#tired`). Requires updating the data model and storage format.

---

## Recommended Build Order

```
Phase 1  →  Phase 2  →  Phase 3 (3.1 + 3.4 first)  →  Phase 4  →  Phase 3 (rest)  →  Phase 5
```

Don't skip Phase 2. Every other feature depends on data surviving a restart.

---

## Quick Reference: Key Files

| File | Purpose |
|---|---|
| `pom.xml` | Build config — fix main class here first |
| `JournalUI.java` | App entry point and current UI |
| `JournalEntry.java` | Data model — extend this for tags/mood |
| `JournalStore.java` | In-memory store — keep for tests |
| `FileStore.java` *(to create)* | Persistent JSON store |
| `JournalController.java` *(to create)* | MVC controller |

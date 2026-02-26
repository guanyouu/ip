# DaddyBot Codebase Instructions

## Architecture Overview
DaddyBot is a Java CLI task manager with a strict "daddy" theme. Core components:
- `DaddyBot.java`: Entry point, initializes storage/UI, loads tasks, starts parser loop
- `Parser.java`: Command dispatcher requiring "please daddy" suffix on all commands
- `Storage.java`: File I/O for `data/daddyslist.txt` using pipe-delimited format (T|D|E | 0|1 | desc | dates)
- `TaskList.java`: Task management (add/delete operations)
- `Ui.java`: Console output with bordered messages (63 underscores)
- Task hierarchy: `Task` (abstract) → `Todo`, `Deadline` (shows days overdue/due), `Event`

## Command Patterns
All user commands must end with "please daddy" (case-insensitive). Parser checks last 12 chars.
- Add tasks: `todo <desc> please daddy`, `deadline <desc> /by YYYY-MM-DD please daddy`, `event <desc> /from YYYY-MM-DD /to YYYY-MM-DD please daddy`
- Manage: `mark <index> please daddy`, `unmark <index> please daddy`, `delete <index> please daddy`, `list please daddy`
- Exit: `bye daddy` (no "please" needed)

## File Format & Persistence
Tasks stored in `data/daddyslist.txt` as: `T | 0 | desc` (Todo), `D | 1 | desc | YYYY-MM-DD` (Deadline), `E | 0 | desc | from | to` (Event)
- 0/1 indicates unmarked/marked
- Storage validates format on load, recreates file on delete (inefficient but simple)
- File created in working directory via `Storage.createFile()`

## Build & Run
- Gradle project with ShadowJar for fat JAR
- **Fix main class in build.gradle**: Change `seedu.duke.Duke` to `daddybot.DaddyBot`
- Run: `./gradlew run` or `java -cp build/libs/duke.jar daddybot.DaddyBot`
- Manual testing: Use `text-ui-test/runtest.bat` (compiles with javac, runs with input.txt, compares ACTUAL.TXT vs EXPECTED.TXT)

## Code Patterns
- Custom `DaddyException` for empty task descriptions
- Task `toString()` formats: `[T][ ] desc`, `[D][X] desc (by: date) - due in (days)`, `[E][ ] desc (from: date to: date)`
- No unit tests; rely on manual text-ui-test
- UI messages wrapped in `Ui.border()` for consistent formatting
- Parser uses substring parsing (no regex), expects exact formats

## Development Notes
- Strict input validation; invalid commands prompt retry
- Magic word enforcement with escalating warnings (5 attempts then exit)
- Dates parsed as `LocalDate` (YYYY-MM-DD only)
- File operations use try-catch with printStackTrace (basic error handling)</content>
<parameter name="filePath">c:\Users\doomc\Desktop\school\cs2103t\ip\.github\copilot-instructions.md
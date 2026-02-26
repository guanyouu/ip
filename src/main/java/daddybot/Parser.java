package daddybot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import daddybot.task.Deadline;
import daddybot.task.Event;
import daddybot.task.Task;
import daddybot.task.Todo;

public class Parser {
    private static final String MAGIC_WORD = "please daddy";
    private static final int MAGIC_WORD_LENGTH = MAGIC_WORD.length();

    // Instance variable instead of static — each session has its own count
    private int magicWordCount = 0;

    /**
     * Parses user input and executes commands.
     */
    public String parse(String input, ArrayList<Task> list) {
        String path = System.getProperty("user.dir");

        if (input == null || input.isBlank()) {
            return "daddy doesn't understand an empty command.";
        }

        assert input != null && !input.isBlank() : "Input should not be null or empty";
        String trimmedInput = input.trim();

        if (!checkDaddy(trimmedInput)) {
            magicWordCount++;
            return hasNoMagicWords(magicWordCount % 5);
        }

        String userCommand = removeMagicWord(trimmedInput).trim();

        String[] parts = userCommand.split("\\s+", 2);
        String command = parts[0].toLowerCase(Locale.ROOT);
        String args = parts.length > 1 ? parts[1].trim() : "";

        try {
            switch (command) {
                case "todo":
                    return todoCommand(args, path, list);
                case "deadline":
                    return deadlineCommand(args, path, list);
                case "event":
                    return eventCommand(args, path, list);
                case "list":
                    return listCommand(args, list);
                case "mark":
                    return markCommand(args, path, list);
                case "unmark":
                    return unmarkCommand(args, path, list);
                case "delete":
                    return deleteCommand(args, path, list);
                case "find":
                    return findCommand(args, list);
                case "snooze":
                    return snoozeCommand(args, path, list);
                default:
                    return "daddy doesn't understand that command.";
            }
        } catch (DaddyException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "daddy got confused with your input: " + e.getMessage();
        }
    }

    // Private — implementation detail of parse()
    private boolean checkDaddy(String input) {
        return input.toLowerCase(Locale.ROOT).endsWith(MAGIC_WORD);
    }

    // Private — implementation detail of parse()
    private String removeMagicWord(String input) {
        return input.substring(0, input.length() - MAGIC_WORD_LENGTH);
    }

    /**
     * Returns a response when the user forgets the magic words.
     * Returns an empty string when magicWordCount % 5 == 0 (intentional reset
     * behaviour).
     */
    private String hasNoMagicWords(int num) {
        switch (num) {
            case 1:
                return "daddy won't do it unless you say 'please daddy'";
            case 2:
                return "what are the magic words?";
            case 3:
                return "are you forgetting something?";
            case 4:
                return "if you continue being naughty, daddy will punish you.";
            case 0:
                return "daddy's getting angry... one more time and daddy is going to leave.";
            default:
                return "daddy is displeased.";
        }
    }

    private String write(Task task, ArrayList<Task> list) throws DaddyException {
        if (task.getDesc().isEmpty()) {
            throw new DaddyException("daddy can't add an empty task.");
        }
        list.add(task);
        return TaskList.addTask(task, list.size());
    }

    // -------------------------------------------------------------------------
    // Command handlers
    // -------------------------------------------------------------------------

    private String todoCommand(String args, String path, ArrayList<Task> list) throws DaddyException {
        if (args.isEmpty()) {
            return "daddy can't add an empty todo.";
        }
        Todo todo = new Todo(args);
        Storage.writeToFile(path + "/data/daddyslist.txt", todo);
        return write(todo, list);
    }

    private String deadlineCommand(String args, String path, ArrayList<Task> list) throws DaddyException {
        if (!args.contains("/by ")) {
            return "daddy needs a /by to know the deadline.";
        }
        int byIndex = args.indexOf("/by ");
        String desc = args.substring(0, byIndex).trim();
        String byStr = args.substring(byIndex + 4).trim();
        LocalDate byDate = LocalDate.parse(byStr);
        Deadline deadline = new Deadline(desc, byDate);
        Storage.writeToFile(path + "/data/daddyslist.txt", deadline);
        return write(deadline, list);
    }

    private String eventCommand(String args, String path, ArrayList<Task> list) throws DaddyException {
        if (!args.contains("/from ") || !args.contains("/to ")) {
            return "daddy needs both /from and /to to know the event time.";
        }
        int fromIndex = args.indexOf("/from ");
        int toIndex = args.indexOf("/to ");
        String eventDesc = args.substring(0, fromIndex).trim();
        LocalDate fromDate = LocalDate.parse(args.substring(fromIndex + 6, toIndex).trim());
        LocalDate toDate = LocalDate.parse(args.substring(toIndex + 4).trim());
        Event event = new Event(eventDesc, fromDate, toDate);
        Storage.writeToFile(path + "/data/daddyslist.txt", event);
        return write(event, list);
    }

    private String listCommand(String args, ArrayList<Task> list) {
        if (list.isEmpty()) {
            return "list is empty.";
        }
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (Task t : list) {
            sb.append(count++).append(". ").append(t.toString()).append("\n");
        }
        return sb.toString();
    }

    private String markCommand(String args, String path, ArrayList<Task> list) {
        if (args.isEmpty()) {
            return "daddy needs to know which task to mark.";
        }
        int markIndex;
        try {
            markIndex = Integer.parseInt(args) - 1;
        } catch (NumberFormatException e) {
            return "daddy needs a valid task number.";
        }
        if (markIndex < 0 || markIndex >= list.size()) {
            return "daddy could not find that task.";
        }
        list.get(markIndex).mark();
        Storage.saveAll(path, list);
        return "daddy is proud of you!\n" + list.get(markIndex).getStatusIcon()
                + " " + list.get(markIndex).toString();
    }

    private String unmarkCommand(String args, String path, ArrayList<Task> list) {
        if (args.isEmpty()) {
            return "daddy needs to know which task to unmark.";
        }
        int unmarkIndex;
        try {
            unmarkIndex = Integer.parseInt(args) - 1;
        } catch (NumberFormatException e) {
            return "daddy needs a valid task number.";
        }
        if (unmarkIndex < 0 || unmarkIndex >= list.size()) {
            return "daddy could not find that task.";
        }
        list.get(unmarkIndex).unmark();
        Storage.saveAll(path, list);
        return "daddy is disappointed...\n" + list.get(unmarkIndex).getStatusIcon()
                + " " + list.get(unmarkIndex).toString();
    }

    private String deleteCommand(String args, String path, ArrayList<Task> list) {
        if (args.isEmpty()) {
            return "daddy needs to know which task to delete.";
        }
        int deleteIndex;
        try {
            deleteIndex = Integer.parseInt(args) - 1;
        } catch (NumberFormatException e) {
            return "daddy needs a valid task number.";
        }
        if (deleteIndex < 0 || deleteIndex >= list.size()) {
            return "daddy could not find that task.";
        }
        return TaskList.deleteTask(list.get(deleteIndex), list, deleteIndex, path);
    }

    private String findCommand(String args, ArrayList<Task> list) {
        if (args.isEmpty()) {
            return "daddy needs a keyword to find.";
        }
        StringBuilder sb = new StringBuilder();
        int foundCount = 1;
        for (Task t : list) {
            if (t.getDesc().contains(args)) {
                sb.append(foundCount++).append(". ").append(t.toString()).append("\n");
            }
        }
        if (foundCount == 1) {
            sb.append("daddy couldn't find any matching tasks.");
        }
        return sb.toString();
    }

    private String snoozeCommand(String args, String path, ArrayList<Task> list) {
        // Expected format: "<taskNumber> /by <days> days"
        // e.g. "2 /by 3 days"
        if (!args.contains("/by ")) {
            return "daddy needs a /by to snooze. Usage: snooze <number> /by <days> days please daddy";
        }

        String[] snoozeParts = args.split("/by ", 2);

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(snoozeParts[0].trim()) - 1;
        } catch (NumberFormatException e) {
            return "daddy needs a valid task number to snooze.";
        }

        int days;
        try {
            days = Integer.parseInt(snoozeParts[1].replace("days", "").trim());
        } catch (NumberFormatException e) {
            return "daddy needs a valid number of days to snooze by.";
        }

        if (taskIndex < 0 || taskIndex >= list.size()) {
            return "daddy could not find that task.";
        }

        try {
            list.get(taskIndex).snooze(days);
            Storage.saveAll(path, list);
            return "daddy snoozed it \uD83D\uDE34\n" + list.get(taskIndex);
        } catch (UnsupportedOperationException e) {
            return "daddy can't snooze this kind of task.";
        }
    }
}

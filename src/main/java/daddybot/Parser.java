package daddybot;

import java.time.LocalDate;
import java.util.ArrayList;

import daddybot.task.Deadline;
import daddybot.task.Event;
import daddybot.task.Task;
import daddybot.task.Todo;

public class Parser {
    private static int magicWordCount = 0;

    /**
     * Parses user input and executes commands.
     */
    public static String parse(String input, ArrayList<Task> list) {
        String path = System.getProperty("user.dir");
        StringBuilder sb = new StringBuilder();

        if (input == null || input.isBlank()) {
            return "daddy doesn't understand an empty command.";
        }
        // We assume input is not null or blank
        assert input != null && !input.isBlank() : "Input should not be null or empty";
        String trimmedInput = input.trim();

        // Check for "please daddy"
        if (!checkDaddy(trimmedInput)) {
            magicWordCount++;
            return hasNoMagicWords(magicWordCount % 5);
        }

        // Remove "please daddy"
        String userCommand = daddyTask(trimmedInput).trim();

        // Split command and arguments
        String[] parts = userCommand.split("\\s+", 2); // [command, args]
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1].trim() : "";

        try {
            switch (command) {
                case "todo":
                    if (args.isEmpty()) {
                        return "daddy can't add an empty todo.";
                    }
                    Todo todo = new Todo(args);
                    assert !todo.getDesc().isEmpty() : "Todo description must not be empty";
                    Storage.writeToFile(path + "/data/daddyslist.txt", todo);
                    return write(todo, list);

                case "deadline":
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

                case "event":
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

                case "list":
                    if (list.isEmpty()) {
                        return "list is empty.";
                    }
                    int count = 1;
                    for (Task t : list) {
                        sb.append(count++).append(". ").append(t.toString()).append("\n");
                    }
                    return sb.toString();

                case "mark":
                    if (args.isEmpty())
                        return "daddy needs to know which task to mark.";
                    int markIndex = Integer.parseInt(args) - 1;
                    if (markIndex < 0 || markIndex >= list.size())
                        return "daddy could not find that task.";
                    assert markIndex >= 0 && markIndex < list.size() : "markIndex out of bounds: " + markIndex;

                    list.get(markIndex).mark();
                    return "daddy is proud of you!\n" + list.get(markIndex).getStatusIcon() + " "
                            + list.get(markIndex).toString();

                case "unmark":
                    if (args.isEmpty())
                        return "daddy needs to know which task to unmark.";
                    int unmarkIndex = Integer.parseInt(args) - 1;
                    if (unmarkIndex < 0 || unmarkIndex >= list.size())
                        return "daddy could not find that task.";
                    list.get(unmarkIndex).unmark();
                    return "daddy is disappointed...\n" + list.get(unmarkIndex).getStatusIcon() + " "
                            + list.get(unmarkIndex).toString();

                case "delete":
                    if (args.isEmpty())
                        return "daddy needs to know which task to delete.";
                    int deleteIndex = Integer.parseInt(args) - 1;
                    if (deleteIndex < 0 || deleteIndex >= list.size())
                        return "daddy could not find that task.";
                    return TaskList.deleteTask(list.get(deleteIndex), list, deleteIndex, path);

                case "find":
                    if (args.isEmpty())
                        return "daddy needs a keyword to find.";
                    int foundCount = 1;
                    for (Task t : list) {
                        if (t.getDesc().contains(args)) {
                            sb.append(foundCount++).append(". ").append(t.toString()).append("\n");
                        }
                    }
                    if (foundCount == 1)
                        sb.append("daddy couldn't find any matching tasks.");
                    return sb.toString();

                default:
                    return "daddy doesn't understand that command.";
            }
        } catch (DaddyException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "daddy got confused with your input: " + e.getMessage();
        }
    }

    public static Boolean checkDaddy(String input) {
        return input.toLowerCase().endsWith("please daddy");
    }

    public static String daddyTask(String input) {
        return input.substring(0, input.length() - 12);
    }

    public static String hasNoMagicWords(int num) {
        switch (num) {
            case 1:
                return "daddy won't do it unless you say 'please daddy'";
            case 2:
                return "what are the magic words?";
            case 3:
                return "are you forgetting something?";
            case 4:
                return "if you continue being naughty, daddy will punish you.";
            case 5:
                return "daddy's getting angry... one more time and daddy is going to leave.";
        }
        return "";
    }

    public static String write(Task task, ArrayList<Task> list) throws DaddyException {
        if (task.getDesc().isEmpty()) {
            throw new DaddyException("daddy can't add an empty task.");
        }
        list.add(task);
        return TaskList.addTask(task, list.size());
    }
}

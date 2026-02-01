package daddybot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import daddybot.Storage;
import daddybot.task.Deadline;
import daddybot.task.Event;
import daddybot.task.Task;
import daddybot.task.Todo;

public class Parser {
    private String input;
    public Parser(String input) {
        this.input = input;
    }

    public static void parse(Scanner scanner, String input, ArrayList<Task> list) {
        int magicWordCount = 0;
        String path = System.getProperty("user.dir");
        while (!input.equals("bye daddy")) {
            int listCount = 0;
            int index = -1;
            if (daddyCheck(input)) {
                switch (input.substring(0, input.indexOf(" "))) {
                    case "todo":
                        Todo todo = new Todo(daddyTask(input).substring(4).trim());
                        try {
                            ifEmpty(todo, list);
                            Storage.writeToFile(path + "/data/daddyslist.txt", todo);

                        } catch (DaddyException e) {
                            Ui.border(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        }
                        break;
                    case "deadline":
                        int seperatorIndex = daddyTask(input).indexOf("/by ");
                        if (seperatorIndex == -1) {
                            Ui.border("daddy needs a /by to know the deadline.");
                            input = scanner.nextLine();
                            continue;
                        }
                        LocalDate byDate = LocalDate.parse(daddyTask(input).substring(seperatorIndex + 3).trim());
                        Deadline deadline = new Deadline(daddyTask(input).substring(9, seperatorIndex).trim(), byDate);
                        try {
                            ifEmpty(deadline, list);
                            Storage.writeToFile(path + "/data/daddyslist.txt", deadline);
                        } catch (DaddyException e) {
                            Ui.border(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        }
                        break;
                    case "event":
                        int seperatorIndex1 = daddyTask(input).indexOf("/from ");
                        int seperatorIndex2 = daddyTask(input).indexOf("/to ");
                        if (seperatorIndex1 == -1 || seperatorIndex2 == -1) {
                            Ui.border("daddy needs both /from and /to to know the event time.");
                            input = scanner.nextLine();
                            continue;
                        }
                        LocalDate fromDate = LocalDate.parse(daddyTask(input).substring(seperatorIndex1 + 5, seperatorIndex2).trim());
                        LocalDate toDate = LocalDate.parse(daddyTask(input).substring(seperatorIndex2 + 3).trim());
                        Event event = new Event(daddyTask(input).substring(5, seperatorIndex1).trim(), fromDate, toDate);
                        try {
                            ifEmpty(event, list);
                            Storage.writeToFile(path + "/data/daddyslist.txt", event);
                        } catch (DaddyException e) {
                            Ui.border(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        }
                        break;
                    case "list":
                        System.out.println("___________________________________________________________________\n");
                        int n = list.size();
                        if (n == 0) {
                            System.out.println("list is empty.");
                        }
                        while (listCount++ < n) {
                            System.out.println(listCount + ". " + list.get(listCount - 1).toString());
                        }
                        System.out.println("___________________________________________________________________\n");
                        break;
                    case "mark":
                        index = Integer.parseInt(daddyTask(input).substring(5).trim()) - 1;
                        if (index < 0 || index >= list.size()) {
                            Ui.border("daddy could not find that task.");
                            input = scanner.nextLine();
                            continue;
                        }
                        list.get(index).mark();
                        Ui.border("daddy is proud of you!\n" + list.get(index).getStatusIcon() + " " + list.get(index).toString());
                        break;
                    case "unmark":
                        index = Integer.parseInt(daddyTask(input).substring(7).trim()) - 1;
                        if (index < 0 || index >= list.size()) {
                            Ui.border("daddy could not find that task.");
                            input = scanner.nextLine();
                            continue;
                        }
                        list.get(index).unmark();
                        Ui.border("daddy is disappointed...\n" + list.get(index).getStatusIcon() + " " + list.get(index).toString());
                        break;
                    case "delete":
                        index = Integer.parseInt(daddyTask(input).substring(7).trim()) - 1;
                        if (index < 0 || index >= list.size()) {
                            Ui.border("daddy could not find that task.");
                            input = scanner.nextLine();
                            continue;
                        }
                        TaskList.deleteTask(list.get(index), list, index, path);
                        break;
                    default:
                        Ui.border("daddy doesn't understand that command.");
                        break;
                }
            } else {
                noMagicWords(magicWordCount);
                magicWordCount++;
                if (magicWordCount > 5) {
                    break;
                }
            }
            input = scanner.nextLine();
        }
        Ui.border("daddy's gonna go now...");
        scanner.close();
    }
    
    public static Boolean daddyCheck(String input) {
        if (input.length() > 12) {
            if (input.substring(input.length() - 12).toLowerCase().equals("please daddy")) {
                return true;
            }            
        }
        return false;
    }

    public static String daddyTask(String input) {
        return input.substring(0, input.length() - 12);
    }

    public static void noMagicWords(int num) {
        switch (num) {
            case 0:
                Ui.border("daddy won't do it unless you say 'please daddy'");
                break;
            case 1:
                Ui.border("what are the magic words?");
                break;
            case 2:
                Ui.border("are you forgetting something?"); 
                break;
            case 3:
                Ui.border("if you continue being naughty, daddy will punish you.");
                break;
            case 4:
                Ui.border("daddy's getting angry... one more time and daddy is going to leave.");
                break;
        }
    }

    public static void ifEmpty(Task task, ArrayList<Task> list) throws DaddyException {
        if (task.getDesc() == "") {
            throw new DaddyException("daddy can't add an empty task.");
        }
        list.add(task);
        TaskList.addTask(task, list.size());
    }
}

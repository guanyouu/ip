import java.util.*;

public class DaddyBot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        border("daddy can add todo, deadline and event tasks.\n" 
        + "todo: type todo <task>.\ndeadline: type deadline <task> /by <date>.\n"
        + "event: type event <task> /from <date> /to <date>.\n"
        + "be sure to add 'please daddy' at the end of your input.\n"
        + "if you're saying bye, say 'bye daddy'.");
        String input = scanner.nextLine().toLowerCase();
        int magicWordCount = 0;
        ArrayList<Task> list = new ArrayList<Task>();
        while (!input.equals("bye daddy")) {
            int listCount = 0;
            int index = -1;
            if (daddyCheck(input)) {
                switch (input.substring(0, input.indexOf(" "))) {
                    case "todo":
                        Todo todo = new Todo(daddyTask(input).substring(4).trim());
                        try {
                            ifEmpty(todo, list);
                        } catch (DaddyException e) {
                            border(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        }
                        break;
                    case "deadline":
                        int seperatorIndex = daddyTask(input).indexOf("/by ");
                        if (seperatorIndex == -1) {
                            border("daddy needs a /by to know the deadline.");
                            input = scanner.nextLine();
                            continue;
                        }
                        Deadline deadline = new Deadline(daddyTask(input).substring(9, seperatorIndex).trim(), daddyTask(input).substring(seperatorIndex + 3).trim());
                        try {
                            ifEmpty(deadline, list);
                        } catch (DaddyException e) {
                            border(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        }
                        break;
                    case "event":
                        int seperatorIndex1 = daddyTask(input).indexOf("/from ");
                        int seperatorIndex2 = daddyTask(input).indexOf("/to ");
                        if (seperatorIndex1 == -1 || seperatorIndex2 == -1) {
                            border("daddy needs both /from and /to to know the event time.");
                            input = scanner.nextLine();
                            continue;
                        }
                        Event event = new Event(daddyTask(input).substring(5, seperatorIndex1).trim(), daddyTask(input).substring(seperatorIndex1 + 5, seperatorIndex2).trim(), daddyTask(input).substring(seperatorIndex2 + 3).trim());
                        try {
                            ifEmpty(event, list);
                        } catch (DaddyException e) {
                            border(e.getMessage());
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
                            border("daddy could not find that task.");
                            input = scanner.nextLine();
                            continue;
                        }
                        list.get(index).mark();
                        border("daddy is proud of you!\n" + list.get(index).getStatusIcon() + " " + list.get(index).toString());
                        break;
                    case "unmark":
                        index = Integer.parseInt(daddyTask(input).substring(7).trim()) - 1;
                        if (index < 0 || index >= list.size()) {
                            border("daddy could not find that task.");
                            input = scanner.nextLine();
                            continue;
                        }
                        list.get(index).unmark();
                        border("daddy is disappointed...\n" + list.get(index).getStatusIcon() + " " + list.get(index).toString());
                        break;
                    default:
                        border("daddy doesn't understand that command.");
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
        border("daddy's gonna go now...");
        scanner.close();
    }

    public static void border(String message) {
        System.out.println("___________________________________________________________________\n");
        System.out.println(message);
        System.out.println("___________________________________________________________________\n");
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
                border("daddy won't do it unless you say 'please daddy'");
                break;
            case 1:
                border("what are the magic words?");
                break;
            case 2:
                border("are you forgetting something?"); 
                break;
            case 3:
                border("if you continue being naughty, daddy will punish you.");
                break;
            case 4:
                border("daddy's getting angry... one more time and daddy is going to leave.");
                break;
        }
    }

    public static void addTask(Task task, int size) {
        border("daddy added: " + task.toString() + "\ntotal tasks: " + size);
    }

    public static void ifEmpty(Task task, ArrayList<Task> list) throws DaddyException {
        if (task.getDesc() == "") {
            throw new DaddyException("daddy can't add an empty task.");
        }
        list.add(task);
        addTask(task, list.size());
    }
}

class Task {
    private String desc;
    private boolean isDone;

    public Task(String desc) {
        this.desc = desc;
        this.isDone = false;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }
}

class Deadline extends Task {
    private String by;

    public Deadline(String desc, String by) {
        super(desc);
        this.by = by;
    }

    public String toString() {
        return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ")";
    }
}

class Todo extends Task {
    public Todo(String desc) {
        super(desc);
    }

    public String toString() {
        return "[T]" + super.getStatusIcon() + " " + super.getDesc();
    }
}

class Event extends Task {
    private String from;
    private String to;

    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return "[E]" + super.getStatusIcon() + " " + super.getDesc() + " (from: " + from + " to: " + to + ")";
    }
}

class DaddyException extends Exception {
    public DaddyException(String message) {
        super(message);
    }
}
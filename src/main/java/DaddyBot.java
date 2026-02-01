import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DaddyBot {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        createDaddyListFile(path);
        Scanner scanner = new Scanner(System.in);
        border("daddy can add todo, deadline and event tasks.\n" 
        + "todo: type todo <task>.\ndeadline: type deadline <task> /by <YYYY-MM-DD>.\n"
        + "event: type event <task> /from <YYYY-MM-DD> /to <YYYY-MM-DD>.\n"
        + "be sure to add 'please daddy' at the end of your input.\n"
        + "if you're saying bye, say 'bye daddy'.");
        String input = scanner.nextLine().toLowerCase();
        int magicWordCount = 0;
        ArrayList<Task> list = new ArrayList<Task>();
        addFromFile(list, path);
        while (!input.equals("bye daddy")) {
            int listCount = 0;
            int index = -1;
            if (daddyCheck(input)) {
                switch (input.substring(0, input.indexOf(" "))) {
                    case "todo":
                        Todo todo = new Todo(daddyTask(input).substring(4).trim());
                        try {
                            ifEmpty(todo, list);
                            writeToFile(path + "/data/daddyslist.txt", todo);

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
                        LocalDate byDate = LocalDate.parse(daddyTask(input).substring(seperatorIndex + 3).trim());
                        Deadline deadline = new Deadline(daddyTask(input).substring(9, seperatorIndex).trim(), byDate);
                        try {
                            ifEmpty(deadline, list);
                            writeToFile(path + "/data/daddyslist.txt", deadline);
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
                            writeToFile(path + "/data/daddyslist.txt", event);
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
                    case "delete":
                        index = Integer.parseInt(daddyTask(input).substring(7).trim()) - 1;
                        if (index < 0 || index >= list.size()) {
                            border("daddy could not find that task.");
                            input = scanner.nextLine();
                            continue;
                        }
                        Task removedTask = list.get(index);
                        list.remove(index);
                        border("daddy removed: '" + removedTask.getDesc() + "'\ntotal tasks: " + list.size());
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

    public static void createDaddyListFile(String path) {
        CreateFile.createFile(path);
    }

    public static void addFromFile(ArrayList<Task> list, String filepath) {
        try {
            Scanner reader = new Scanner(new File(filepath));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (validateFormat(line)) {
                    String[] parts = line.split("|");
                    switch (parts[0].trim()) {
                        case "T":
                            Todo todo = new Todo(parts[2].trim());
                            if (parts[1].trim().equals("1")) {
                                todo.mark();
                            }
                            list.add(todo);
                            break;
                        case "D":
                            Deadline deadline = new Deadline(parts[2].trim(), parts[3].trim());
                            if (parts[1].trim().equals("1")) {
                                deadline.mark();
                            }
                            list.add(deadline);
                            break;
                        case "E":
                            Event event = new Event(parts[2].trim(), parts[3].trim(), parts[4].trim());
                            if (parts[1].trim().equals("1")) {
                                event.mark();
                            }
                            list.add(event);
                            break;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateFormat(String line) {
        String[] parts = line.split("|");
        if (parts[0].trim().equals("T")) {
            if (parts[1].trim().equals("0") || parts[1].trim().equals("1")) {
                if (parts[2] != null) {
                    return true;
                }
            }
        }
        if (parts[0].trim().equals("D")) {
            if (parts[1].trim().equals("0") || parts[1].trim().equals("1")) {
                if (parts[2] != null) {
                    if (parts[3] != null){
                        return true;
                    }
                }
            }
        }
        if (parts[0].trim().equals("E")) {
            if (parts[1].trim().equals("0") || parts[1].trim().equals("1")) {
                if (parts[2] != null) {
                    if (parts[3] != null) {
                        if (parts[4] != null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void writeToFile(String filepath, Task task) {
        try {
            FileWriter writer = new FileWriter(filepath, true);
            if (task instanceof Todo todo) {
                writer.write("T | " + (todo.getStatusIcon().equals("[X]") ? "1" : "0") + " | " + todo.getDesc() + "\n");
            } else if (task instanceof Deadline deadline) {
                writer.write("D | " + (deadline.getStatusIcon().equals("[X]") ? "1" : "0") + " | " + deadline.getDesc() + " | " + deadline.getBy() + "\n");
            } else if (task instanceof Event event) {
                writer.write("E | " + (event.getStatusIcon().equals("[X]") ? "1" : "0") + " | " + event.getDesc() + " | " + event.getFrom() + " | " + event.getTo() + "\n");
            } else {
                writer.close();
                throw new IllegalArgumentException("daddy could not write the task to file");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    public String toString() {
        LocalDate now = LocalDate.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, by);
        if (daysBetween < 0) {
            return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ") - overdue by (" + Math.abs(daysBetween) + ") days";
        } else {
            return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ") - due in (" + daysBetween + ") days";
        }
    }

    public String getBy() {
        return this.by;
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

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }
}

class DaddyException extends Exception {
    public DaddyException(String message) {
        super(message);
    }
}

class CreateFile {
    public static void createFile(String path) {
        try {
            File dir = new File(path + "/data");
            dir.mkdirs();

            File file = new File(path + "/data/daddyslist.txt");
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

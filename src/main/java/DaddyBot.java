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
        Storage.createFile(path);
        Scanner scanner = new Scanner(System.in);
        Ui ui = new Ui();
        String input = scanner.nextLine().toLowerCase();
        ArrayList<Task> list = new ArrayList<Task>();
        Storage.addFromFile(list, path);
        Parser.parse(scanner, input, list);
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
    private LocalDate by;

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

    public LocalDate getBy() {
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
    private LocalDate from;
    private LocalDate to;

    public Event(String desc, LocalDate from, LocalDate to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return "[E]" + super.getStatusIcon() + " " + super.getDesc() + " (from: " + from + " to: " + to + ")";
    }

    public LocalDate getFrom() {
        return this.from;
    }

    public LocalDate getTo() {
        return this.to;
    }
}

class DaddyException extends Exception {
    public DaddyException(String message) {
        super(message);
    }
}

class Ui {
    public Ui() {
        border("daddy can add todo, deadline and event tasks.\n\n" 
        + "todo: type todo <task>.\ndeadline: type deadline <task> /by <YYYY-MM-DD>.\n"
        + "event: type event <task> /from <YYYY-MM-DD> /to <YYYY-MM-DD>.\n\n"
        + "to mark a task as done, type mark <task number>.\nto unmark a task, type unmark <task number>.\n"
        + "to delete a task, type delete <task number>.\nto view all tasks, type list.\n\n"
        + "be sure to add 'please daddy' at the end of your input.\n"
        + "if you're saying bye, say 'bye daddy'.");
    }

    public static void border(String message) {
        System.out.println("___________________________________________________________________\n");
        System.out.println(message);
        System.out.println("___________________________________________________________________\n");
    }
}

class Storage {
    private String filepath;

    public Storage(String filepath) {
        this.filepath = filepath;
    }

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

    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        file.delete();
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

    public static void addFromFile(ArrayList<Task> list, String filepath) {
        try {
            Scanner reader = new Scanner(new File(filepath + "/data/daddyslist.txt"));
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
                            Deadline deadline = new Deadline(parts[2].trim(), LocalDate.parse(parts[3].trim()));
                            if (parts[1].trim().equals("1")) {
                                deadline.mark();
                            }
                            list.add(deadline);
                            break;
                        case "E":
                            Event event = new Event(parts[2].trim(), LocalDate.parse(parts[3].trim()), LocalDate.parse(parts[4].trim()));
                            if (parts[1].trim().equals("1")) {
                                event.mark();
                            }
                            list.add(event);
                            break;
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

class Parser {
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

class TaskList {
    private ArrayList<Task> list;
    public TaskList(ArrayList<Task> list) {
        this.list = list;
    }

    public ArrayList<Task> getList() {
        return this.list;
    }

    public static void addTask(Task task, int size) {
        Ui.border("daddy added: " + task.toString() + "\ntotal tasks: " + size);
    }

    public static void deleteTask(Task task, ArrayList<Task> list, int index, String path) {
        Task removedTask = list.get(index);
        list.remove(index);
        Storage.deleteFile(path + "/data/daddyslist.txt");
        Storage.createFile(path);
        Storage.addFromFile(list, path);
        Ui.border("daddy removed: '" + removedTask.getDesc() + "'\ntotal tasks: " + list.size());
    }

    public static void markTask(Task task, int index) {
        
    }
}
package daddybot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import daddybot.task.Deadline;
import daddybot.task.Event;
import daddybot.task.Task;
import daddybot.task.Todo;

/**
 * Handles file storage operations for DaddyBot.
 */
public class Storage {

    /**
     * Creates the data directory and daddyslist.txt file if they do not exist.
     *
     * @param path The base path where the data directory should be created.
     */
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

    /**
     * Deletes the specified file.
     *
     * @param filepath The path of the file to be deleted.
     */
    public static void deleteFile(String filepath) {
        new File(filepath).delete();
    }

    /**
     * Validates the format of a line from the storage file.
     *
     * @param line The line to be validated.
     * @return True if the format is valid, false otherwise.
     */
    public static boolean isValid(String line) {
        String[] parts = line.split("\\|");
        try {
            String type = parts[0].trim();
            String status = parts[1].trim();
            if (!status.equals("0") && !status.equals("1")) {
                return false;
            }
            switch (type) {
                case "T":
                    return parts.length >= 3;
                case "D":
                    return parts.length >= 4;
                case "E":
                    return parts.length >= 5;
                default:
                    return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Reads tasks from the storage file and adds them to the provided list.
     *
     * @param list     The list to which tasks will be added.
     * @param filepath The base path where the daddyslist.txt file is located.
     */
    public static void addFromFile(ArrayList<Task> list, String filepath) {
        try {
            Scanner reader = new Scanner(new File(filepath + "/data/daddyslist.txt"));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (!isValid(line)) {
                    continue;
                }
                String[] parts = line.split("\\|");
                boolean isDone = parts[1].trim().equals("1");
                switch (parts[0].trim()) {
                    case "T": {
                        Todo todo = new Todo(parts[2].trim());
                        if (isDone)
                            todo.mark();
                        list.add(todo);
                        break;
                    }
                    case "D": {
                        Deadline deadline = new Deadline(parts[2].trim(), LocalDate.parse(parts[3].trim()));
                        if (isDone)
                            deadline.mark();
                        list.add(deadline);
                        break;
                    }
                    case "E": {
                        Event event = new Event(parts[2].trim(), LocalDate.parse(parts[3].trim()),
                                LocalDate.parse(parts[4].trim()));
                        if (isDone)
                            event.mark();
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

    /**
     * Appends a single task to the storage file.
     *
     * @param filepath The path of the storage file.
     * @param task     The task to be written.
     */
    public static void writeToFile(String filepath, Task task) {
        try (FileWriter writer = new FileWriter(filepath, true)) {
            writer.write(taskToLine(task) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rewrites the entire storage file from the current in-memory list.
     * Used after mark, unmark, delete, and snooze operations.
     *
     * @param path The base path (not including /data/daddyslist.txt).
     * @param list The current list of tasks.
     */
    public static void saveAll(String path, ArrayList<Task> list) {
        String filepath = path + "/data/daddyslist.txt";
        deleteFile(filepath);
        createFile(path);
        try (FileWriter writer = new FileWriter(filepath, false)) {
            for (Task task : list) {
                writer.write(taskToLine(task) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a task to its storage line format.
     *
     * @param task The task to convert.
     * @return A pipe-delimited string representing the task.
     */
    private static String taskToLine(Task task) {
        String status = task.getStatusIcon().equals("[X]") ? "1" : "0";
        if (task instanceof Todo) {
            return "T | " + status + " | " + task.getDesc();
        } else if (task instanceof Deadline d) {
            return "D | " + status + " | " + d.getDesc() + " | " + d.getBy();
        } else if (task instanceof Event e) {
            return "E | " + status + " | " + e.getDesc() + " | " + e.getFrom() + " | " + e.getTo();
        } else {
            throw new IllegalArgumentException("daddy could not write the task to file");
        }
    }
}

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
    private String filepath;

    public Storage(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Creates the data directory and daddyslist.txt file if they do not exist.
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
     * @param filepath The path of the file to be deleted.
     */

    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        file.delete();
    }

    /**
     * Validates the format of a line from the storage file.
     * @param line The line to be validated.
     * @return True if the format is valid, false otherwise.
     */

    public static boolean validateFormat(String line) {
        String[] parts = line.split("\\|");
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

    /**
     * Reads tasks from the storage file and adds them to the provided list.
     * @param list The list to which tasks will be added.
     * @param filepath The base path where the daddyslist.txt file is located.
     */

    public static void addFromFile(ArrayList<Task> list, String filepath) {
        try {
            Scanner reader = new Scanner(new File(filepath + "/data/daddyslist.txt"));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (validateFormat(line)) {
                    String[] parts = line.split("\\|");
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

    /**
     * Writes a task to the storage file.
     * @param filepath The path of the storage file.
     * @param task The task to be written to the file.
     */

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
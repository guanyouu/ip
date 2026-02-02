package daddybot;

import java.util.ArrayList;
import daddybot.task.Task;

/**
 * TaskList class to manage the list of tasks.
 */

public class TaskList {
    private ArrayList<Task> list;

    public TaskList(ArrayList<Task> list) {
        this.list = list;
    }

    /**
     * Gets the list of tasks.
     * 
     * @return ArrayList of tasks.
     */

    public ArrayList<Task> getList() {
        return this.list;
    }

    /**
     * Adds a task to the list.
     * 
     * @param task the task to be added.
     * @param size the new size of the list.
     */

    public static void addTask(Task task, int size) {
        Ui.addBorder("daddy added: " + task.toString() + "\ntotal tasks: " + size);
    }

    /**
     * Deletes a task from the list.
     * 
     * @param task  the task to be deleted.
     * @param list  the list of tasks.
     * @param index the index of the task to be deleted.
     * @param path  the path to the storage file.
     */

    public static void deleteTask(Task task, ArrayList<Task> list, int index, String path) {
        Task removedTask = list.get(index);
        list.remove(index);
        Storage.deleteFile(path + "/data/daddyslist.txt");
        Storage.createFile(path);
        Storage.addFromFile(list, path);
        Ui.addBorder("daddy removed: '" + removedTask.getDesc() + "'\ntotal tasks: " + list.size());
    }
}

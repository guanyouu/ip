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
     * Confirms a task was added and returns a response string.
     * NOTE: Storage writing is handled by the caller (Parser) — do not write here.
     *
     * @param task the task that was added.
     * @param size the new size of the list.
     */
    public static String addTask(Task task, int size) {
        return "daddy added: " + task.toString() + "\ntotal tasks: " + size;
    }

    /**
     * Deletes a task from the list and rebuilds the storage file.
     *
     * @param task  the task to be deleted.
     * @param list  the list of tasks.
     * @param index the index of the task to be deleted.
     * @param path  the path to the storage file.
     */
    public static String deleteTask(Task task, ArrayList<Task> list, int index, String path) {
        Task removedTask = list.get(index);
        list.remove(index);
        Storage.saveAll(path, list);
        return "daddy removed: '" + removedTask.getDesc() + "'\ntotal tasks: " + list.size();
    }
}

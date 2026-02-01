package daddybot;

import java.util.ArrayList;
import daddybot.task.Task;

public class TaskList {
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
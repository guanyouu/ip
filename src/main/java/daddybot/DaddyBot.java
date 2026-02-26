package daddybot;

import java.util.ArrayList;

import daddybot.task.Task;

/**
 * The main class of DaddyBot application.
 */
public class DaddyBot {

    private static ArrayList<Task> list = new ArrayList<Task>();
    private Parser parser = new Parser();

    /**
     * Runs the DaddyBot application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        Storage.createFile(path);
        Storage.addFromFile(list, path);
    }

    public String getResponse(String input) {
        return parser.parse(input, list);
    }
}

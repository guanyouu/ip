package daddybot;

import java.util.ArrayList;

import daddybot.task.Task;

/**
 * The main class of DaddyBot application.
 */
public class DaddyBot {

    /**
     * Runs the DaddyBot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        Storage.createFile(path);

    }

    public String getResponse(String input) {
        ArrayList<Task> list = new ArrayList<Task>();
        Storage.addFromFile(list, System.getProperty("user.dir"));
        return Parser.parse(input, list);
        
    }
}

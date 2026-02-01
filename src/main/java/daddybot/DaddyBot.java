package daddybot;

import java.util.ArrayList;
import java.util.Scanner;
import daddybot.task.Task;


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

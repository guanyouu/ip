package daddybot;

/**
 * Ui class to handle user interactions.
 */
public class Ui {

    /**
     * Displays the welcome message and instructions.
     */
    public static void start() {
        addBorder("daddy can add todo, deadline and event tasks.\n\n" 
        + "todo: type todo <task>.\ndeadline: type deadline <task> /by <YYYY-MM-DD>.\n"
        + "event: type event <task> /from <YYYY-MM-DD> /to <YYYY-MM-DD>.\n\n"
        + "to mark a task as done, type mark <task number>.\nto unmark a task, type unmark <task number>.\n"
        + "to delete a task, type delete <task number>.\nto view all tasks, type list.\n\n"
        + "be sure to add 'please daddy' at the end of your input.\n"
        + "if you're saying bye, say 'bye daddy'.");
    }

    /**
     * Displays a border around a message.
     * 
     * @param message Message to be displayed between the border.
     */
    public static void addBorder(String message) {
        System.out.println("___________________________________________________________________\n");
        System.out.println(message);
        System.out.println("___________________________________________________________________\n");
    }
}

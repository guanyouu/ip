package daddybot.task;

/**
 * Represents a Todo task.
 */

public class Todo extends Task {
    public Todo(String desc) {
        super(desc);
    }

    /**
     * Returns the string representation of the Todo task.
     *
     * @return A string representing the Todo task.
     */

    @Override
    public String toString() {
        return "[T]" + super.getStatusIcon() + " " + super.getDesc();
    }
}

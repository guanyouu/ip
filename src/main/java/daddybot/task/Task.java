package daddybot.task;

/**
 * Represents a general task with a description and completion status.
 */
public class Task {
    private String desc;
    private boolean isDone;

    public Task(String desc) {
        this.desc = desc;
        this.isDone = false;
    }

    /**
     * Gets the description of the task.
     *
     * @return The description of the task.
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * Gets the status icon representing whether the task is done.
     *
     * @return A string representing the status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Snoozes the task by the given number of days.
     * Subclasses with a date field (e.g. Deadline) should override this.
     * 
     * @param days Number of days to snooze by.
     * @throws UnsupportedOperationException if the task type cannot be snoozed.
     */
    public void snooze(int days) {
        throw new UnsupportedOperationException("This task type cannot be snoozed.");
    }
}

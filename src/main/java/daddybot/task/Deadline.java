package daddybot.task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a deadline task with a description and a due date.
 */

public class Deadline extends Task {
    private LocalDate by;

    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    /**
     * Returns a string representation of the deadline task, including its status,
     * description, due date, and the number of days until or past the deadline.
     *
     * @return A formatted string representing the deadline task.
     */

    @Override
    public String toString() {
        LocalDate now = LocalDate.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, by);
        if (daysBetween < 0) {
            return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ") - overdue by (" + Math.abs(daysBetween) + ") days";
        } else {
            return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ") - due in (" + daysBetween + ") days";
        }
    }

    /**
     * Gets the due date of the deadline task.
     *
     * @return The due date as a LocalDate object.
     */

    public LocalDate getBy() {
        return this.by;
    }
}
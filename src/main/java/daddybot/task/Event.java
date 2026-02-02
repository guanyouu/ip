package daddybot.task;

import java.time.LocalDate;

/**
 * Represents an event task with a description, start date, and end date.
 */

public class Event extends Task {
    private LocalDate from;
    private LocalDate to;

    public Event(String desc, LocalDate from, LocalDate to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a string representation of the event task.   
     * @return String representation of the event task.
     */

    @Override
    public String toString() {
        return "[E]" + super.getStatusIcon() + " " + super.getDesc() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Gets the start date of the event.
     * @return LocalDate representing the start date.
     */

    public LocalDate getFrom() {
        return this.from;
    }

    /**
     * Gets the end date of the event.
     * @return LocalDate representing the end date.
     */

    public LocalDate getTo() {
        return this.to;
    }
}
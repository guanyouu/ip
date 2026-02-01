package daddybot.task;

import java.time.LocalDate;

public class Event extends Task {
    private LocalDate from;
    private LocalDate to;

    public Event(String desc, LocalDate from, LocalDate to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return "[E]" + super.getStatusIcon() + " " + super.getDesc() + " (from: " + from + " to: " + to + ")";
    }

    public LocalDate getFrom() {
        return this.from;
    }

    public LocalDate getTo() {
        return this.to;
    }
}
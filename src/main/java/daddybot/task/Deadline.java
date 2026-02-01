package daddybot.task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Deadline extends Task {
    private LocalDate by;

    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    public String toString() {
        LocalDate now = LocalDate.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, by);
        if (daysBetween < 0) {
            return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ") - overdue by (" + Math.abs(daysBetween) + ") days";
        } else {
            return "[D]" + super.getStatusIcon() + " " + super.getDesc() + " (by: " + by + ") - due in (" + daysBetween + ") days";
        }
    }

    public LocalDate getBy() {
        return this.by;
    }
}
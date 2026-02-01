package daddybot.task;

public class Todo extends Task {
    public Todo(String desc) {
        super(desc);
    }

    public String toString() {
        return "[T]" + super.getStatusIcon() + " " + super.getDesc();
    }
}

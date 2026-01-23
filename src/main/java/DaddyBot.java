import java.util.*;

public class DaddyBot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        border("what can daddy do for you?\nbe sure to add 'please daddy' at the end of your input.\nif you're saying bye, say 'bye daddy'.");
        String input = scanner.nextLine();
        int magicWordCount = 0;
        ArrayList<Task> list = new ArrayList<Task>();
        while (!input.equals("bye daddy")) {
            int listCount = 0;
            if (daddyCheck(input)) {
                if (!(input.substring(0, 4).equals("list") || input.substring(0, 4).equals("mark") || input.substring(0, 6).equals("unmark"))) {
                    list.add(new Task(daddyTask(input)));
                    border("daddy added: " + daddyTask(input));
                } else if (input.substring(0, 4).equals("mark")) { 
                    int index = Integer.parseInt(daddyTask(input).substring(5).trim()) - 1;
                    if (index < 0 || index >= list.size()) {
                        border("daddy could not find that task.");
                        input = scanner.nextLine();
                        continue;
                    }
                    list.get(index).mark();
                    border("daddy is proud of you!\n" + list.get(index).getStatusIcon() + " " + list.get(index).getDesc());
                } else if (input.substring(0, 6).equals("unmark")) {
                    int index = Integer.parseInt(daddyTask(input).substring(7).trim()) - 1;
                    if (index < 0 || index >= list.size()) {
                        border("daddy could not find that task.");
                        input = scanner.nextLine();
                        continue;
                    }
                    list.get(index).unmark();
                    border("daddy is disappointed...\n" + list.get(index).getStatusIcon() + " " + list.get(index).getDesc());
                } else {
                    System.out.println("___________________________________________________________________\n");
                    int n = list.size();
                    while (listCount++ < n) {
                        System.out.println(listCount + "." + list.get(listCount - 1).getStatusIcon() + " " + list.get(listCount - 1).getDesc());
                    }
                    System.out.println("___________________________________________________________________\n");
                }
            } else {
                noMagicWords(magicWordCount);
                magicWordCount++;
                if (magicWordCount > 5) {
                    break;
                }
            }
            input = scanner.nextLine();
        }
        border("daddy's gonna go now...");
        scanner.close();
    }

    public static void border(String message) {
        System.out.println("___________________________________________________________________\n");
        System.out.println(message);
        System.out.println("___________________________________________________________________\n");
    }

    public static Boolean daddyCheck(String input) {
        if (input.length() > 12) {
            if (input.substring(input.length() - 12).equals("please daddy")) {
                return true;
            }            
        }
        return false;
    }

    public static String daddyTask(String input) {
        return input.substring(0, input.length() - 12);
    }

    public static void noMagicWords(int num) {
        switch (num) {
            case 0:
                border("daddy won't do it unless you say 'please daddy'");
                break;
            case 1:
                border("what are the magic words?");
                break;
            case 2:
                border("are you forgetting something?"); 
                break;
            case 3:
                border("if you continue being naughty, daddy will punish you.");
                break;
            case 4:
                border("daddy's getting angry... one more time and daddy is going to leave.");
                break;
        }
    }
}

class Task {
    private String desc;
    private boolean isDone;

    public Task(String desc) {
        this.desc = desc;
        this.isDone = false;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }
}
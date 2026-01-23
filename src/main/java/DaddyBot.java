import java.util.*;

public class DaddyBot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        daddyIntro();
        String input = scanner.nextLine();
        int magicWordCount = 0;
        ArrayList<Task> list = new ArrayList<Task>();
        while (!input.equals("bye daddy")) {
            int listCount = 0;
            if (daddyCheck(input)) {
                if (!(input.substring(0, 4).equals("list") || input.substring(0, 4).equals("mark") || input.substring(0, 6).equals("unmark"))) {
                    list.add(new Task(daddyTask(input)));
                    System.out.println("___________________________________________________________________\n");
                    System.out.println("daddy added: " + daddyTask(input));
                    System.out.println("___________________________________________________________________\n");
                } else if (input.substring(0, 4).equals("mark")) { 
                    int index = Integer.parseInt(daddyTask(input).substring(5).trim()) - 1;
                    if (index < 0 || index >= list.size()) {
                        System.out.println("___________________________________________________________________\n");
                        System.out.println("daddy could not find that task.");
                        System.out.println("___________________________________________________________________\n");
                        input = scanner.nextLine();
                        continue;
                    }
                    list.get(index).mark();
                    System.out.println("___________________________________________________________________\n");
                    System.out.println("daddy is proud of you:");
                    System.out.println(list.get(index).getStatusIcon() + " " + list.get(index).getDesc());
                    System.out.println("___________________________________________________________________\n");
                } else if (input.substring(0, 6).equals("unmark")) {
                    int index = Integer.parseInt(daddyTask(input).substring(7).trim()) - 1;
                    if (index < 0 || index >= list.size()) {
                        System.out.println("___________________________________________________________________\n");
                        System.out.println("daddy could not find that task.");
                        System.out.println("___________________________________________________________________\n");
                        input = scanner.nextLine();
                        continue;
                    }
                    list.get(index).unmark();
                    System.out.println("___________________________________________________________________\n");
                    System.out.println("daddy is disappointed...");
                    System.out.println(list.get(index).getStatusIcon() + " " + list.get(index).getDesc());
                    System.out.println("___________________________________________________________________\n");
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
        daddyBye();
        scanner.close();
    }

    public static void daddyIntro() {
        System.out.println("___________________________________________________________________\n");
        System.out.println("what can daddy do for you?");
        System.out.println("be sure to add 'please daddy' at the end of your input.");
        System.out.println("if you're saying bye, say 'bye daddy'.");
        System.out.println("___________________________________________________________________\n");
    }

    public static void daddyBye() {
        System.out.println("___________________________________________________________________\n");
        System.out.println("daddy's gonna go now...");
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
                System.out.println("___________________________________________________________________\n");
                System.out.println("daddy won't do it unless you say 'please daddy'");
                System.out.println("___________________________________________________________________\n");
                break;
            case 1:
                System.out.println("___________________________________________________________________\n");
                System.out.println("what are the magic words?");
                System.out.println("___________________________________________________________________\n");
                break;
            case 2:
                System.out.println("___________________________________________________________________\n");
                System.out.println("are you forgetting something?"); 
                System.out.println("___________________________________________________________________\n");           
                break;
            case 3:
                System.out.println("___________________________________________________________________\n");
                System.out.println("if you continue being naughty, daddy will punish you.");
                System.out.println("___________________________________________________________________\n");
                break;
            case 4:
                System.out.println("___________________________________________________________________\n");
                System.out.println("daddy's getting angry... one more time and daddy is going to leave.");
                System.out.println("___________________________________________________________________\n");
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
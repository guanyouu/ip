import java.util.*;

public class DaddyBot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        daddyIntro();
        String input = scanner.nextLine();
        int count = 0;
        ArrayList<String> list = new ArrayList<String>();
        while (!input.equals("bye daddy")) {
            if (daddyCheck(input)) {
                if (!input.substring(0, input.length() - 12).equals("list ")){
                    list.add(input.substring(0, input.length() - 12));
                    System.out.println("___________________________________________________________________\n");
                    System.out.println("daddy added: " + input.substring(0, input.length() - 12));
                    System.out.println("___________________________________________________________________\n");
                } else {
                    System.out.println("___________________________________________________________________\n");
                    int listCount = 0;
                    int n = list.size();
                    while (listCount++ < n) {
                        System.out.println(listCount + ". " + list.get(listCount - 1));
                    }
                    System.out.println("___________________________________________________________________\n");
                }
            } else {
                noMagicWords(count);
                count++;
                if (count > 5) {
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
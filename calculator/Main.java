package calculator;

import java.util.Scanner;

public class Main {

    private static final Scanner SCAN = new Scanner(System.in);

    public static void main(String[] args) {
        runSumLoop();
        System.out.println("Bye!");
    }

    private static void runSumLoop() {
        String userInput;
        do {
            userInput = SCAN.nextLine();
            if (!userInput.isBlank() && !"/exit".equals(userInput)) {
                String[] userInputFields = userInput.split(" ");
                if (userInputFields.length < 2) {
                    System.out.println(userInputFields[0]);
                } else {
                    int num1 = Integer.parseInt(userInputFields[0]);
                    int num2 = Integer.parseInt(userInputFields[1]);
                    System.out.println(num1 + num2);
                }
            }
        } while (!"/exit".equals(userInput));
    }
}

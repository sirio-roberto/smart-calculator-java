package calculator;

import java.util.Arrays;
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
            if ("/help".equals(userInput)) {
                System.out.println("The program calculates the sum of numbers");
            }
            else if (!userInput.isBlank() && !"/exit".equals(userInput)) {
                String[] userInputFields = userInput.split(" ");
                if (Arrays.stream(userInputFields).allMatch(p -> p.matches("-?\\d+"))) {
                    int sum = Arrays.stream(userInputFields).map(Integer::parseInt).reduce(0, Integer::sum);
                    System.out.println(sum);
                } else {
                    System.out.println("Only numbers are supported");
                }
            }
        } while (!"/exit".equals(userInput));
    }
}

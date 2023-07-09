package calculator;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner SCAN = new Scanner(System.in);

    private static final String[] VALID_COMMANDS = {"/help", "/exit"};

    public static void main(String[] args) {
        runSumLoop();
        System.out.println("Bye!");
    }

    private static void runSumLoop() {
        String userInput;
        do {
            userInput = SCAN.nextLine();
            if (userInput.matches("/+.*") && !Arrays.asList(VALID_COMMANDS).contains(userInput)) {
                System.out.println("Unknown command");
            }
            else if ("/help".equals(userInput)) {
                System.out.println("The program calculates the sum and subtraction of numbers");
            }
            else if (!userInput.isBlank() && !"/exit".equals(userInput)) {
                userInput = removeExtraSpacesAndPlus(userInput);
                userInput = convertMinus(userInput);
                String[] userInputFields = userInput.split(" ");

                if (isValidExpression(userInputFields)) {
                    int total = Integer.parseInt(userInputFields[0]);
                    for (int i = 1; i < userInputFields.length; i += 2) {
                        int nextNumber = Integer.parseInt(userInputFields[i + 1]);
                        switch (userInputFields[i]) {
                            case "+" -> total += nextNumber;
                            case "-" -> total -= nextNumber;
                        }
                    }
                    System.out.println(total);
                } else {
                    System.out.println("Invalid expression");
                }
            }
        } while (!"/exit".equals(userInput));
    }

    private static boolean isValidExpression(String[] userInputFields) {
        for (int i = 0; i < userInputFields.length; i+= 2) {
            if (!userInputFields[i].matches("[-+]?\\d+")) {
                return false;
            }
        }
        if (userInputFields.length > 1) {
            for (int i = 1; i < userInputFields.length; i += 2) {
                if (!userInputFields[i].matches("[-+]")) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String convertMinus(String userInput) {
        Pattern minusPattern = Pattern.compile("-{2,}");
        String[] expressionArray = userInput.split(" ");
        for (int i = 0; i < expressionArray.length; i++) {
            Matcher multipleMinusMatcher = minusPattern.matcher(expressionArray[i]);
            if (multipleMinusMatcher.matches()) {
                // if the amount of minus is even, we replace it with plus, otherwise we replace with one minus
                expressionArray[i] = expressionArray[i].length() % 2 == 0 ? "+" : "-";
            }
        }
        return String.join(" ", expressionArray);
    }

    private static String removeExtraSpacesAndPlus(String userInput) {
        Matcher matcherSpaces = Pattern.compile("\\s{2,}").matcher(userInput);
        String result = matcherSpaces.replaceAll(" ");

        Matcher matcherPlus = Pattern.compile("\\+{2,}").matcher(result);
        result = matcherPlus.replaceAll("+");
        return result;
    }
}

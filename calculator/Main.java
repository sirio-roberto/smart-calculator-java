package calculator;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner SCAN = new Scanner(System.in);

    private static final String[] VALID_COMMANDS = {"/help", "/exit"};

    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        String userInput;
        do {
            userInput = SCAN.nextLine();

            if (userInput.matches("/+.*")) {
                handleCommands(userInput);
            }
            else if (!userInput.isBlank() && !"/exit".equals(userInput)) {
                userInput = removeExtraSpaces(userInput);
                userInput = removeExtraPluses(userInput);
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

    private static void handleCommands(String userInput) {
        if (userInput.matches("/+.*") && !Arrays.asList(VALID_COMMANDS).contains(userInput)) {
            System.out.println("Unknown command");
        } else if ("/help".equals(userInput)) {
            // TODO: adjust helper text before last commit
            System.out.println("The program calculates the sum and subtraction of numbers");
        } else {
            System.out.println("Bye!");
        }
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

    private static String removeExtraSpaces(String userInput) {
        Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(userInput);
        return spacesMatcher.replaceAll(" ");
    }

    private static String removeExtraPluses(String userInput) {
        Matcher plusesMatcher = Pattern.compile("\\+{2,}").matcher(userInput);
        return plusesMatcher.replaceAll("+");
    }
}

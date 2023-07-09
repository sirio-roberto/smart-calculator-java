package calculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner SCAN = new Scanner(System.in);

    private static final String[] VALID_COMMANDS = {"/help", "/exit"};

    private static final Map<String, Integer> variables = new HashMap<>();

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
                if (isAssignment(userInput)) {
                    handleAssignment(userInput);
                } else {
                    handleExpression(userInput);
                }
            }
        } while (!"/exit".equals(userInput));
    }

    private static void handleExpression(String userInput) {
        userInput = removeExtraPluses(userInput);
        userInput = convertMinus(userInput);
        String[] userInputFields = userInput.split(" ");

        if (userInputFields.length == 1) {
            if (isValidIdentifier(userInputFields[0])) {
                if (variables.containsKey(userInputFields[0])) {
                    System.out.println(variables.get(userInputFields[0]));
                } else {
                    System.out.println("Unknown variable");
                }
            } else {
                System.out.println("Invalid identifier");
            }
        } else if (isValidExpression(userInputFields)) {
            String[] convertedExp = getVariableValues(userInputFields);
            int total = Integer.parseInt(convertedExp[0]);
            for (int i = 1; i < convertedExp.length; i += 2) {
                int nextNumber = Integer.parseInt(convertedExp[i + 1]);
                switch (convertedExp[i]) {
                    case "+" -> total += nextNumber;
                    case "-" -> total -= nextNumber;
                }
            }
            System.out.println(total);
        } else {
            System.out.println("Invalid expression");
        }
    }

    private static String[] getVariableValues(String[] userInputFields) {
        String[] result = userInputFields.clone();
        for (int i = 0; i < result.length; i += 2) {
            if (!isNumeric(result[i])) {
                result[i] = String.valueOf(variables.get(result[i]));
            }
        }
        return result;
    }

    private static void handleAssignment(String userInput) {
        userInput = removeAllSpaces(userInput);
        String[] userInputFields = userInput.split("=");
        String key = userInputFields[0];

        if (isValidIdentifier(key)) {
            if (userInputFields.length != 2) {
                System.out.println("Invalid assignment");
            } else {
                String valueStr = userInputFields[1];
                if (isNumeric(valueStr)) {
                    Integer value = Integer.valueOf(valueStr);
                        variables.put(key, value);
                    } else {
                        if (isValidIdentifier(valueStr)) {
                            if (variables.containsKey(valueStr)) {
                                variables.put(key, variables.get(valueStr));
                            } else {
                                System.out.println("Unknown variable");
                            }
                        } else {
                            System.out.println("Invalid assignment");
                        }
                    }
                }
            } else {
                System.out.println("Invalid identifier");
        }
    }

    private static boolean isNumeric(String value) {
        Matcher numericMatcher = Pattern.compile("[-+]?\\d+").matcher(value);
        return numericMatcher.matches();
    }

    private static boolean isValidIdentifier(String key) {
        Matcher lettersMatcher = Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE).matcher(key);
        return lettersMatcher.matches();
    }

    private static boolean isAssignment(String userInput) {
        Matcher equalMatcher = Pattern.compile("=").matcher(userInput);
        return equalMatcher.find();
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
            if (!isNumeric(userInputFields[i])) {
                if (!variables.containsKey(userInputFields[i])) {
                    return false;
                }
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

    private static String removeAllSpaces(String userInput) {
        Matcher allSpacesMatcher = Pattern.compile("\\s+").matcher(userInput);
        return allSpacesMatcher.replaceAll("");
    }

    private static String removeExtraSpaces(String userInput) {
        Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(userInput.trim());
        return spacesMatcher.replaceAll(" ");
    }

    private static String removeExtraPluses(String userInput) {
        Matcher plusesMatcher = Pattern.compile("\\+{2,}").matcher(userInput);
        return plusesMatcher.replaceAll("+");
    }
}

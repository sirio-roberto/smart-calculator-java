package calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner SCAN = new Scanner(System.in);

    private static final String[] VALID_COMMANDS = {"/help", "/exit"};

    private static final String ALLOWED_CHARS_REGEX = "[a-zA-Z0-9 +/*^()-]+";

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

        if (userInputFields.length == 1 && !hasOperators(userInput)) {
            if (isNumeric(userInputFields[0])) {
                System.out.println(userInputFields[0]);
            }
            else if (isValidIdentifier(userInputFields[0])) {
                if (variables.containsKey(userInputFields[0])) {
                    System.out.println(variables.get(userInputFields[0]));
                } else {
                    System.out.println("Unknown variable");
                }
            } else {
                System.out.println("Invalid identifier");
            }
        } else if (isValidExpression(userInput)) {
            userInput = removeAllSpaces(userInput);
            String[] infixArray = convertToInfixArray(userInput);
            getVariableValues(infixArray);

            List<String> postfixArray = convertInfixToPostfix(infixArray);

            System.out.println(evaluatePostfixExpression(postfixArray));
        } else {
            System.out.println("Invalid expression");
        }
    }

    private static long evaluatePostfixExpression(List<String> postfixArray) {
        Deque<String> stack = new ArrayDeque<>();

        for (String s: postfixArray) {
            if (isNumeric(s)) {
                stack.offerLast(s);
            } else {
                long num1 = Long.parseLong(stack.pollLast());
                long num2 = Long.parseLong(stack.pollLast());
                long result = evaluateExpression(num2, num1, s);
                stack.offerLast(String.valueOf(result));
            }
        }

        return Long.parseLong(stack.pollLast());
    }

    private static long evaluateExpression(long num1, long num2, String operator) {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            case "^" -> (long) Math.pow(num1, num2);
            default -> 0;
        };
    }

    private static List<String> convertInfixToPostfix(String[] infixArray) {
        List<String> result = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String s: infixArray) {
            if (isNumeric(s)) {
                result.add(s);
            } else if (isOperator(s)) {
                if (stack.isEmpty() || "(".equals(stack.peekLast())) {
                    stack.offerLast(s);
                } else if (getPrecedence(s) > getPrecedence(stack.peekLast())) {
                    stack.offerLast(s);
                } else {
                    while (stack.peekLast() != null && getPrecedence(stack.peekLast()) >= getPrecedence(s)) {
                        result.add(stack.pollLast());
                    }
                    stack.offerLast(s);
                }
            } else if ("(".equals(s)) {
                stack.offerLast(s);
            } else if (")".equals(s)) {
                while (stack.peekLast() != null && !"(".equals(stack.peekLast())) {
                    result.add(stack.pollLast());
                }
                stack.pollLast();
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pollLast());
        }
        return result;
    }

    private static int getPrecedence(String s) {
        return switch (s) {
            case "^" -> 3;
            case "/", "*" -> 2;
            case "+", "-" -> 1;
            default -> 0;
        };
    }

    private static boolean isOperator(String s) {
        return s.matches("[-+/*^]");
    }

    private static String[] convertToInfixArray(String userInput) {
        String pattern = "(?<=[+\\-*/^()])|(?=[+\\-*/^()])";
        return userInput.split(pattern);
    }

    private static boolean hasOperators(String userInput) {
        Matcher operatorsMatcher = Pattern.compile("[^-][+/*^()-]").matcher(userInput);
        return operatorsMatcher.find();
    }

    private static void getVariableValues(String[] input) {
        for (int i = 0; i < input.length; i++) {
            if (isValidIdentifier(input[i])) {
                input[i] = String.valueOf(variables.get(input[i]));
            }
        }
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
        // TODO: maybe we need to remove the sign part because it can break the infix transformation logic
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

    private static boolean isValidExpression(String userInput) {
        Matcher allowedCharsMatcher = Pattern.compile(ALLOWED_CHARS_REGEX).matcher(userInput);
        if (!allowedCharsMatcher.matches()) {
            return false;
        }

        Matcher doubleDivOrMultMatcher = Pattern.compile("[/*]{2,}").matcher(userInput);
        if (doubleDivOrMultMatcher.find()) {
            return false;
        }

        int openBrackets = 0;
        int closeBrackets = 0;
        for (char c: userInput.toCharArray()) {
            if (c == '(') {
                openBrackets++;
            }
            if (c == ')') {
                closeBrackets++;
            }
        }
        if (openBrackets != closeBrackets) {
            return false;
        }

        String[] userInputFields = userInput.replaceAll("[()]", "").split("\\s?[-+/*^]\\s?");

        for (String field : userInputFields) {
            if (!isNumeric(field)) {
                if (!variables.containsKey(field)) {
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

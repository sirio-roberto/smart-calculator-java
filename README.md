Package and Imports

The code is in the calculator package. It imports several classes and packages necessary for the functionality of the program:

    java.math.BigDecimal and java.math.BigInteger: Used for precise decimal and integer calculations.
    java.util.*: Provides utility classes such as Scanner, ArrayDeque, and ArrayList.

Constants and Data Structures

The code defines various constants and data structures:

    VALID_COMMANDS: An array containing the valid commands recognized by the program.
    ALLOWED_CHARS_REGEX: A regular expression defining the allowed characters in user input.
    variables: A Map<String, BigInteger> to store user-defined variables and their corresponding values.

Main Method and Program Execution

The main() method is the entry point of the program. It calls the runApp() method to start the program execution.
runApp() Method

The runApp() method handles the main execution logic of the program. It continuously prompts the user for input and processes it until the user enters the "/exit" command.
Input Handling and Command Processing

The runApp() method checks user input for commands or expressions. It distinguishes between commands and arithmetic expressions using regular expressions. If a command is detected, it calls the handleCommands() method to process it. Otherwise, it checks if the input is an assignment or an arithmetic expression and calls the respective handler methods (handleAssignment() or handleExpression()).
handleCommands() Method

The handleCommands() method processes the recognized commands. It supports the "/help" command, which displays helper text, and the "/exit" command, which terminates the program. If an unknown command is entered, it prints an "Unknown command" message.
handleAssignment() Method

The handleAssignment() method handles variable assignments. It parses and validates user input to ensure it follows the correct format for variable assignments. If the assignment is valid, it stores the assigned value in the variables map using the variable name as the key. If the assigned value is a numeric value or a valid variable name, it performs the assignment accordingly. Otherwise, it prints an "Invalid assignment" or "Unknown variable" message.
handleExpression() Method

The handleExpression() method processes arithmetic expressions entered by the user. It validates the expression, converts it to postfix notation, evaluates it, and prints the result. It uses several helper methods like convertToInfixArray(), convertInfixToPostfix(), evaluatePostfixExpression(), and evaluateExpression() to perform these operations.
Arithmetic Expression Evaluation

The code utilizes the Shunting Yard Algorithm to convert infix notation to postfix notation (convertInfixToPostfix()) and then evaluates the postfix expression (evaluatePostfixExpression()). It maintains a stack (Deque) to store intermediate results during the conversion and evaluation process. The evaluateExpression() method performs the actual arithmetic calculations based on the operator and operands.
Input Validation

The code includes validation checks to ensure that user input is valid. It verifies the format and characters of expressions, detects unbalanced parentheses, checks if variables are valid identifiers, and ensures that expressions contain only valid variables or numeric values.
Miscellaneous Helper Methods

The code also includes several helper methods (isNumeric(), isValidIdentifier(), isAssignment(), etc.) that perform specific checks or transformations, such as checking if a string is a numeric value or a valid variable identifier, detecting variable assignments, removing extra spaces, etc.

Overall, the code provides a simple calculator program with support for basic arithmetic operations, variable assignments, and command handling. However, please note that the code might have some areas for improvement and potential edge cases to handle based on your specific requirements and use cases.

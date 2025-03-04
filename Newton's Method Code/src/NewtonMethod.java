/*
 * @author Allen Feng
 * 
 * This program uses Newton's method to approximate a root of any non-trig/log function (these are text based).
 * Trig/log functions may be implemented later.
 *
 * Test cases (eval):
 * 5(-x+1)
 * Make sure the program can evaluate parenthesis and do negative variables correctly
 * 
 * 5+(5+6*-4+8-9)+(10*4) + 5
 * Should be: 5+-20+40+5
 * = 30
 * Make sure the program evaluates multiple sets of parenthesis
 * Also, order of operations
 * (55+x)(88-x^2)
 * Test adjacent parenthesis
 *
 * Test cases (nMethod):
 * y = (x+10)^(1/3) - 3
 * y' = (1/3)*(x+10)^(-2/3)
 * Correct answer: 17
 * 
 * y = (55+x)(88+x^2)
 * y' = 3x^2 + 110x + 88
 * Correct answer: -55
 * 
 * y = (1/8)x^2
 * y' = (1/4)x
 * Correct answer: 0
 * 
 * //TODO: See if this case can be resolved, currently prints NaN after first iteration
 * Final test:
 * y = (5-x)/(23-x^3)+86(x^(1/3))
 * y' = ((23-x^3)(-x)-(5-x)(-3x^2))/(23-x^3)^2+29x^(-2/3)
 * Correct answers: 0, 2.8446
 * 
 */

import java.io.*;
import java.util.*;

public class NewtonMethod {
    //Angles/Trig functions to be implemented
    //Maybe make function + derivative global variables?

    private class Expression {
        private String expression;
        private char[] expArr;
        private char variable = ' ';
        private List<String> operators, operands;
        private boolean isValid = false;
        
        public Expression(String expression) {
            this.expression = expression;
            expArr = expression.toCharArray();

            //Find variable
            for (int index = 0; index < expArr.length; index++) {
                if (Character.isLetter(expArr[index])) {
                    variable = expArr[index];
                }
            }

            operators = findOperators();
            operands = findOperands();

            isValid = testValidInput();

            /* */
            for(String operator : operators) {
                System.out.print(operator + " ");
            }
            System.out.println();

            for(String operand : operands) { 
                System.out.print(operand + " ");
            }
            System.out.println(); 
        }

        public Expression(List<String> operators, List<String> operands) {
            this.operators = operators;
            this.operands = operands;
        }

        public Expression(Expression other) {
            expression = other.expression;
            expArr = other.expArr;
            variable = other.variable;
            operators = new ArrayList<>(other.operators);
            operands = new ArrayList<>(other.operands);
            isValid = other.isValid;
        }

        public Expression() {};

        /*
         * Evaluates a function from left to right.
         * Order of operations will be implemented... eventually.
         * 10-3-22 Actually sorta works
         * 10-4-22 It works!
         * 
         * The function can NOT have any spaces.
         * 
         * @param function   the function to evaluate for variable = input
         * @param input      the value to evaluate the function at
         * 
         * Likely redundant as of 1-4-25
         */
        /* 
        public double eval(double input) {
            String nextFunc = "", pExp;
            

            for (int index = 0; index < operands.size(); index++) {
                //Replace all variables with the input value specified
                if (operands.get(index).equals(String.valueOf(variable))) {
                    operands.set(index, Double.toString(input)); 
                } else if (operands.get(index).equals("-" + String.valueOf(variable))) {
                    operands.set(index, "-" + input);
                }


                //Make sure only one set of parenthesis is put into the equation
                if (index < operators.size()) {
                    if (operators.get(index).equals("(")) {
                        nextFunc += operators.get(index); 
                    }else if (index != operators.size() - 1 && operators.get(index).equals(")")) {
                        nextFunc += operands.get(index);
                        nextFunc += operators.get(index);
                        nextFunc += operators.get(index + 1);
                        index++;
                    } else {
                        nextFunc += operands.get(index);
                        nextFunc += operators.get(index);
                    }
                } else {
                    nextFunc += operands.get(index);
                }
            }
            char[] temp = nextFunc.toCharArray();

            //implement a constant function
            //System.out.println("Function (unsimplified) evaluated at " + input + ": " + nextFunc);
            //pExp = "";
            //Evaluate expressions in parenthesis
            if (nextFunc.contains("(") || nextFunc.contains("[")) {
                //Prepare to rewrite nextFunc
                nextFunc = "";

                for (int index = 0; index < temp.length; index++) {
                    //Initialize/reset pExp
                    pExp = "";
                    //Find the expression(s) in parenthesis, evaluate them, add the simplified expression to nextFunc and finish re-writing the rest of the function.
                    
                    if (temp[index] == '(' || temp[index] == '[') {
                        index++;
                        while (temp[index] != ')' && temp[index] != ']' && index < temp.length) {
                            pExp += String.valueOf(temp[index]);
                            index++;
                        }

                        //System.out.println("Expression in parenthesis: " + pExp);
                        if (pExp.contains("(") || pExp.contains("[")) {
                            eval(pExp, input);
                        }

                        nextFunc += eval(pExp);

                        //System.out.println("Evaluated expression 1 more time");
                        //System.out.println("Rewriting function: " + nextFunc);
                        continue;
                    } else {
                        nextFunc += temp[index];
                    }
                }
            }

            //System.out.println("It should be -4.0");
            //System.out.println("The function after the parenthetical statement has been evaluated at " + input + ": " + nextFunc);
            try {
                Double answer = Double.parseDouble(nextFunc);
                return answer;
            } catch (NumberFormatException e) {
                return eval(nextFunc);
            }
        } */

        public double eval(double input) {
            //Create new expression with variables replaced in input so that original expression is not changed
            Expression replaced = new Expression(this);

            //Replace all variables with the input value specified
            for (int index = 0; index < operands.size(); index++) {
                if(operands.get(index).contains(String.valueOf(variable))) {
                    StringBuilder token = new StringBuilder("");

                    if(operands.get(index).contains("-")) {
                        token.append('-');
                    }

                    token.append(input);
                    replaced.operands.set(index, token.toString());
                }
                
                /* 
                if (operands.get(index).equals(String.valueOf(variable))) {
                    operands.set(index, Double.toString(input)); 
                } else if (operands.get(index).equals("-" + String.valueOf(variable))) {
                    operands.set(index, "-" + input);
                }*/
            }
            
            return replaced.eval();
        }

        /*
         * Evaluates an expression from left to right.
         * Order of operations will be implemented eventually.
         * 10-3-22 Almost done
         * 10-4-22 Order of operations works
         * The function can NOT have any spaces.
         * 
         * @param expression   the expression to evaluate
         */
        public double eval() {
            String operator;
            double operand1 = 0, operand2 = 0;
            int opIndex = -1;
            
            //Evaluate all parentheticals, innermost first
            for(int index = 0; index < operators.size(); index++) {
                //Search for open parenthesis or "-(", indicating that the result of the parenthetical should be negated
                //System.out.println(operators.get(index).contains("("));

                if(operators.get(index).contains("(")) {
                    //Index of open parenthesis
                    int first = index;

                    //Number of OTHER unmatched parentheses
                    int numUnmatched = 0;

                    //So that the current open parenthesis is not counted in numUnmatched
                    index++;

                    //Find closing parenthesis - if no other unmatched parentheses exist, then the correct closing parenthesis has been found
                    while(index < operators.size() && ! (operators.get(index).equals(")") && numUnmatched == 0) ){
                        if(operators.get(index).equals("(")) numUnmatched++;
                        else if(operators.get(index).equals(")")) numUnmatched--;

                        index++;
                    }

                    //Index is now index of closing parenthesis - create new expression of the parenthetical
                    int last = index;

                    //Note that subList is inclusive of the first boundary and exclusive of the second - want to exclude both outermost parentheses
                    Expression pExp = new Expression(new ArrayList<>(operators.subList(first + 1, last)), new ArrayList<>(operands.subList(first + 1, last + 1)));

                    //Replace closing parenthesis in operands with the evaluated parenthetical, delete everything before it
                    if (operators.get(first).contains("-"))
                        operands.set(last + 1, Double.toString(0 - pExp.eval()));
                    else 
                        operands.set(last + 1, Double.toString(pExp.eval()));
                    
                    operands.subList(first, last + 1).clear();

                    //Clear all operators in the parenthetical
                    operators.subList(first, last + 1).clear();
                    
                    index = first - 1;

                    /* 
                    for(String operator2 : operators) {
                        System.out.print(operator2 + " ");
                    }
                    System.out.println();

                    for(String operand : operands) { 
                        System.out.print(operand + " ");
                    }
                    System.out.println(); */
                }
            }

            while(!operators.isEmpty()) {
                if (operators.contains("^")) {
                    opIndex = operators.indexOf("^");
                    operand1 = Double.parseDouble(operands.get(opIndex));
                    operand2 = Double.parseDouble(operands.get(opIndex + 1));
                    operand1 = Math.pow(operand1, operand2);
                } else if (operators.contains("/")) {
                    opIndex = operators.indexOf("/");
                    operand1 = Double.parseDouble(operands.get(opIndex));
                    operand2 = Double.parseDouble(operands.get(opIndex + 1));
                    operand1 /= operand2;
                } else if (operators.contains("*")) {
                    opIndex = operators.indexOf("*");
                    operand1 = Double.parseDouble(operands.get(opIndex));
                    operand2 = Double.parseDouble(operands.get(opIndex + 1));
                    operand1 *= operand2;
                } else if (operators.contains("+")) {
                    opIndex = operators.indexOf("+");
                    operand1 = Double.parseDouble(operands.get(opIndex));
                    operand2 = Double.parseDouble(operands.get(opIndex + 1));
                    operand1 += operand2;
                }
                else if (operators.contains("-")) {
                    opIndex = operators.indexOf("-");
                    operand1 = Double.parseDouble(operands.get(opIndex));
                    operand2 = Double.parseDouble(operands.get(opIndex + 1));
                    operand1 -= operand2;
                }

                //Replace index in operands with the answer, remove used operators/operands
                if (opIndex != -1) {
                    operators.remove(opIndex);
                    operands.set(opIndex, Double.toString(operand1));
                    operands.remove(opIndex + 1);
                }
            }

            //System.out.println("The evaluated expression: " + nextExp);

            return Double.parseDouble(operands.get(0));
        }

        /*
         * Returns an ArrayList<String> containing all of the operators in a function.
         * The function can NOT have any spaces. Remove all spaces before passing to this function.
         * 10-3-22 Negative numbers implemented
         * 12-31-24 Implemented helper methods to better check for negative signs
         *
         * @param function   the function to find the operators of
         * @return ArrayList of operators
         */
        public ArrayList<String> findOperators() {
            ArrayList<String> operators = new ArrayList<>();

            //Search through the function
            for (int index = 0; index < expArr.length; index++) {
                if (isOperator(index)) {
                    operators.add(String.valueOf(expArr[index])); 
                } 
                //Keep parentheses in operator list to maintain correct order
                else if (isParenthesis(index)) {
                    //Place extra multiplication operator when two parenthetical statements are adjacent
                    if (index != 0 && ((expArr[index] == '(' && expArr[index - 1] == ')') || (expArr[index] == '[' && expArr[index - 1] == ']'))) {
                        operators.add("*");
                    }

                    operators.add(Character.toString(expArr[index])); 
                }
                //Place an extra multiplication operator when a number is written adjacent to a variable/parenthetical
                else if (index != (expArr.length - 1) && Character.isDigit(expArr[index]) && (expArr[index + 1] == variable || expArr[index + 1] == '(' || expArr[index + 1] == '[')) {
                    operators.add("*");
                } 
                else if (index != 0 && ((Character.isDigit(expArr[index]) && (expArr[index - 1] == ')' || expArr[index - 1] == ']')) 
                                            || (expArr[index] == variable && (expArr[index - 1] == ')' || expArr[index - 1] == ']')))) {
                    operators.add("*"); 
                }
                //Rewrite negative signs as -1 * operand
                else if(isNegativeSign(index)) {
                    operators.add("*");
                }
                else if(index != 0 && expArr[index] == variable && expArr[index - 1] == variable) {
                    operators.add("*");
                }
            }

            return operators;
        }

        /*
         * Returns an ArrayList<String> containing all of the operands in a function.
         * Type String for easier concatenation later.
         * The function can NOT have any spaces.
         * 10-3-22 Negative numbers implemented
         * 12-31-24 Implemented helper methods to better check for negative signs
         * 
         * @param function   the function to find the operands of
         */
        public ArrayList<String> findOperands() {
            ArrayList<String> operands = new ArrayList<>();

            for (int index = 0; index < expArr.length; index++) {
                StringBuilder token = new StringBuilder("");

                if(isNegativeSign(index)) {
                    //If there is a negative sign, treat it as -1 * operand
                    operands.add("-1");
                    index++;
                }
                
                if(isOperand(index) && !(expArr[index] == variable)) {
                    //add first number to token
                    token.append(expArr[index]);

                    while(hasMoreDigs(index)) {
                        token.append(expArr[index + 1]);
                        index++;
                    }
                    
                    operands.add(token.toString());
                }
                else if (expArr[index] == variable) {
                    operands.add(Character.toString(expArr[index])); 
                }
                else if (isParenthesis(index)) {
                    operands.add(Character.toString(expArr[index]));
                }
            }

            return operands;
        }

        /*
        * Tests if a given function is valid based on its number of operands, operators, parenthesis, and brackets.
        * The function can NOT have any spaces.
        * 
        * @param function   the function to test the validity of
        */
        public boolean testValidInput() {
            int numOpenP = 0;
            int numOpenB = 0;
            int numCloseP = 0;
            int numCloseB = 0;
            char decimal = ' ';

            //Search through function - check decimal validity and count parentheses/brackets
            for (int index = 0; index < expArr.length; index++) {
                char ch = expArr[index];
                //If more than one letter is used
                if (Character.isLetter(ch) && ch != variable) {
                    System.out.println("Please use only a single variable in your input.");
                    return false;
                }

                //If c is a decimal point, store '.' in decimal. 
                if (ch == '.' && decimal != ch) {
                    decimal = '.'; 
                } //If there is another decimal point in the same term, the function is not valid input.
                else if (decimal == ch) {
                    System.out.println("Please do not put more than one decimal point in a single number.");
                    return false;
                }
                else if (isOperator(index) || isParenthesis(index)) {
                    decimal = ' '; 
                }else if (ch == '(') {
                    numOpenP++; 
                }else if (ch == ')') {
                    numCloseP++; 
                }else if (ch == '[') {
                    numOpenB++; 
                }else if (ch == ']') {
                    numCloseB++;
                }
            }

            //Check what went wrong and return an error message
            //TODO: Check if this first if statement is really necessary or if it can be replaced by a return statement
            if (numOpenP == numCloseP && numOpenB == numCloseB && operators.size() + 1 == operands.size()) {
                return true; 
            } else if (expression.equals("")) {
                System.out.println("Please enter something.");
                return false;
            } else if (numOpenP != numCloseP) {
                System.out.println("Please check that your parentheses match.");
                return false;
            } else if (numOpenB != numCloseB) {
                System.out.println("Please check that your brackets match.");
                return false;
            } else {
                for (int index = 0; index < expArr.length - 1; index++) {
                    //If the next char after an operator is not a digit or open parenthesis
                    if (isOperator(index) && (!(Character.isDigit(expArr[index + 1])) && expArr[index + 1] != '(')) {
                        System.out.println("Please check the \"" + expArr[index] + "\" operator.");
                    } //If the last char is an operator
                    else if (index == expArr.length - 1) {
                        if (isOperator(index)) {
                            System.out.println("Please check the \"" + expArr[index] + "\" operator.");
                        }
                    }
                    /* else {
                        System.out.println("Something went wrong. Please try again.");
                    } */
                }
                return false;
            }
        }

        public boolean isOperand(int index) {
            //Is an operand if it is a number or the variable
            //Note that this checks a SINGLE char
            return Character.isDigit(expArr[index]) || expArr[index] == variable || expArr[index] == '.';
        }

        public boolean isParenthesis(int index) {
            return expArr[index] == '(' || expArr[index] == ')' || expArr[index] == '[' || expArr[index] == ']';
        }
        
        public boolean isOperator(int index) {
            if (expArr[index] == '+' || expArr[index] == '-' || expArr[index] == '*' || expArr[index] == '/' || expArr[index] == '^') {
                if(!isNegativeSign(index)) return true;
            }
            return false;
        }

        public boolean isNegativeSign(int index) {
            if(expArr[index] != '-') return false;
            //If still running, the char == '-'

            //If the first char is '-'
            if(index == 0) return true;

            //If the '-' is preceded by an operator/open parenthesis and followed by an operand, open parenthesis, or negative sign, it is a negative sign
            if (index != 0                 && (isOperator(index - 1) || expArr[index - 1] == '(')
             && index != expArr.length - 1 && (isOperand(index + 1)  || expArr[index + 1] == '(' || isNegativeSign(index + 1))) {
                return true; 
            }

            return false;
        }

        public boolean hasMoreDigs(int index) {
            //If this is the last character, than there are no more digits
            if(index >= expArr.length - 1)
                return false;
            //If this digit is followed by an operator, than all digits for the number have been read
            else if(index < expArr.length - 1 && (isOperator(index + 1) || isParenthesis(index + 1) || expArr[index + 1] == variable))
                return false;

            return true;
        }
    }

    
    //private static boolean radians = true;
    //private static boolean degrees = false;
    //private static boolean gradians = false;
    private static Expression function, derivative;

    public static void main(String[] args) {
        NewtonMethod driver = new NewtonMethod();
        
        boolean done = false;
        boolean validInput;
        
        String stop;

        //x and y value for tangent line, slope of tangent line (derivative at x) k, number of iterations
        double x = 0;
        int iterations = 0;


        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (!done) {
                function = driver.new Expression(); 
                derivative = function;
                
                while (!function.isValid) {
                    System.out.print("Enter the original function: \ny = ");

                    String expression = br.readLine().replaceAll(" ", "");
                    function = driver.new Expression(expression);

                    System.out.println();
                    //System.out.println(variable);
                }
                 
                while (!derivative.isValid) {
                    System.out.print("Enter the function's derivative: \ny' = ");

                    String dExpression = br.readLine().replaceAll("\\s", "");
                    derivative = driver.new Expression(dExpression);

                    System.out.println();
                }
                
                validInput = false;
                while (!validInput) {
                    try {
                        if(function.variable == ' ') function.variable = 'x';

                        System.out.print("Enter the starting point: \n" + function.variable + " = ");
                        x = Double.parseDouble(br.readLine());
                        validInput = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                    
                    System.out.println();
                }

                validInput = false;
                while (!validInput) {
                    try {
                        System.out.print("How many iterations would you like to run? ");
                        iterations = Integer.parseInt(br.readLine());
                        validInput = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }

                    System.out.println();
                }

                //System.out.println(function);
                for (int index = 0; index < iterations; index++) {
                    System.out.print("Iteration " + (index + 1) + ": ");
                    x = nMethod(function, derivative, x);
                    System.out.println(x);
                }
                
                System.out.println();

                //System.out.println(function.eval(x));
                validInput = false;

                while (!validInput) {
                    System.out.print("Continue? Y/N: ");
                    stop = br.readLine();
                    
                    if (stop.replaceAll("\\s", "").equalsIgnoreCase("y")) {
                        validInput = true;
                        done = false;

                        System.out.println();
                        break;
                    } else if (stop.replaceAll("\\s", "").equalsIgnoreCase("n")) {
                        validInput = true;
                        done = true;
                    } else {
                        System.out.println("Please enter a valid input - either Y or N. ");
                        System.out.println();
                    }
                }
            }
            //System.out.println("Program complete.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static double nMethod(Expression function, Expression derivative, double input) {
        //System.out.println(function.eval(input));
        //System.out.println(derivative.eval(input));

        double nextInput = input - (function.eval(input) / derivative.eval(input));

        return nextInput;
    }
}

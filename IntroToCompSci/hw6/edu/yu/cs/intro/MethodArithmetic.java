package edu.yu.cs.intro;

import java.util.Scanner;

public class MethodArithmetic {
    /**
     * Each non-main method must return a string with the final equation
     * Each non-main method must deal with an arbitrary number of operands solicited from the user via getOperands, except for power which only takes two operands
     * Main method loops until user types "off"
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        //loop until the user "turns the calculator off"
        while (true) {
            System.out.println("Specify an operation. You can enter \"add\", \"sub\", \"mul\", \"div\", \"pow\"");
            System.out.println("To turn off the calculator, enter \"off\"");
            System.out.print("Please enter your operation: ");
            String operation = scanner.next();
            try{
                if (isArithmetic(operation)) {
                    System.out.println(arithmetic(operation, scanner));
                }
                else if (operation.equalsIgnoreCase("pow")) {
                    System.out.println(exponent(scanner));
                }
                else if (operation.equalsIgnoreCase("off")) {
                    break;
                }
            }catch(ArithmeticException e){
                System.out.println("Please only enter mathematically valid inputs!");
            }
        }

    }
    /**
     * Get however many operands the user decides to input.
     * The first value in the returned array, i.e. index 0, is an integer indicating how many operands were input. Let's call it "COUNT".
     * Index 1 through index COUNT hold the actual operand values.
     */
    public static int[] getOperands(Scanner scanner) {
        /*
         * do the following until the user enters the word "done":
         * 1) ask the user for an operand by printing the following: "Please enter an operand. To stop entering operands, enter \"done\""
         * 2) if the user enters an int,
         *     2a) if there is no room left in the operands array, then call arrayLengthDoubler to double its size
         *     2b) read the int into the next available slot in the int[] to be returned
         * 3) if the user enters "done", stop looping
         * After you've exited the loop, return the operand array to the caller, with the number of operands entered by the user as the first int in the array, and the actual values starting from the second int and on.
         */
        int [] operands = new int [2];
        int count = 0;
        while (true){
        	System.out.println("Please enter an operand. To stop entering operands, enter \"done\" ");
        	String operand = scanner.next();
        	if (operand.equalsIgnoreCase("done")){
        		break;
        	}
        	else {
        		if (count+1 == operands.length){
        			operands = arrayLengthDoubler(operands);
        			operands [count+1] = Integer.parseInt(operand);
        		}
        		else{
        			operands [count+1] = Integer.parseInt(operand);
        		}

        	}
        	count++;
        	operands[0] = count;
        
        }

        return operands;
    }

    /**
     * given an array, create a new array twice its size, copy all the contents from the old array to the new
     * one, and return the new array
     */
    public static int[] arrayLengthDoubler(int[] array) {
    	int [] copy = new int [(array.length)*2];
    	for (int i = 0; i < array.length; i++){
    		copy [i] = array [i];
    	}
        return copy;
    }

    /**
     * prompt the user to enter the base and the exponent, and return a string of the following form:
     * BASE raised to the EXPONENT power = RESULT
     * This method need only deal with positive integers
     */
    public static String exponent(Scanner scanner) {
    	System.out.println("Please enter the base: ");
    	int base = scanner.nextInt();
    	System.out.println("Please enter the exponent (positive numbers only): ");
    	int exponent = scanner.nextInt();
    	int result = (int) Math.pow(base, exponent);
        return base + " raised to the " + exponent + " power = " + result;
    }

    /**
     * is the given operation basic arithmetic, i.e. add, sub, mul, or div?
     */
    public static boolean isArithmetic(String operation) {
    	if (operation.equalsIgnoreCase("add")|| operation.equalsIgnoreCase("sub")||operation.equalsIgnoreCase("mul")||operation.equalsIgnoreCase("div")){
    		return true;
    	}
        return false;
    }

    /**
     * Call getOperands to get the operands.
     * Then call the correct arithmetic method (add, sub, mul, or div), and pass it the operands.
     * Whichever arithmetic is called returns the complete equation to this method, and this method returns it to main.
     * Each arithmetic method should return a complete equation of the math it did as a String, e.g. "2+3+5=10", "100/4/5=5", etc.
     */
    public static String arithmetic(String operation, Scanner scanner) throws ArithmeticException{
    	int [] operands = getOperands(scanner);
    	String equation = "";
    	
    	if (operation.equalsIgnoreCase("add")){
    		equation = add(operands);
    	}

    	else if (operation.equalsIgnoreCase("sub")){
    		equation = subtract(operands);
    	}

    	else if (operation.equalsIgnoreCase("mul")){
    		equation = multiply(operands);
    	}

    	else if (operation.equalsIgnoreCase("div")){
    		equation = divide(operands);
    	}
        return equation;
    }
    /**
     * Add up the numbers in the operands array.
     * See the comments on the getOperands method to understand the contents of the array
     * */
    public static String add(int[] operands) throws ArithmeticException{
    	//int total = operands[1];
    	long total = (long) operands[1];
    	String equation = "";
    	for (int i = 2; i <= operands[0]; i++){
    		total += (long) operands[i];
    		if ((int) total != total){
    			throw new ArithmeticException ("integer overflow");
    		}
    	}
    	for (int i = 1; i < operands[0]; i++){
    		equation += (operands[i] + "+");
    	}


    	equation += operands[operands[0]] + "=" + total;
        return equation;
    }
    /**
     * Subtract the numbers in the operands array.
     * See the comments on the getOperands method to understand the contents of the array
     * */
    public static String subtract(int[] operands) throws ArithmeticException{
        long total = (long) operands[1];
    	String equation = "";
    	for (int i = 2; i <= operands[0]; i++){
    		total -= (long) operands[i];
    		if ((int) total != total){
    			throw new ArithmeticException ("integer overflow");
    		}
    	}
    	for (int i = 1; i < operands[0]; i++){
    		equation += (operands[i] + "-");
    	}
    	equation += operands[operands[0]] + "=" + total;
        return equation;
    }
    /**
     * Multiply the numbers in the operands array.
     * See the comments on the getOperands method to understand the contents of the array
     * */
    public static String multiply(int[] operands) throws ArithmeticException{
        long total = operands[1];
    	String equation = "";
    	for (int i = 2; i <= operands[0]; i++){
    		total *= operands[i];
    		if ((int) total != total){
    			throw new ArithmeticException ("integer overflow");
    		}
    	}
    	for (int i = 1; i < operands[0]; i++){
    		equation += (operands[i] + "*");
    	}
    	equation += operands[operands[0]] + "=" + total;
        return equation;
    }
    /**
     * Divide the numbers in the operands array.
     * See the comments on the getOperands method to understand the contents of the array
     * */
    public static String divide(int[] operands) throws ArithmeticException{
        double total = operands[1];
    	String equation = "";
    	for (int i = 2; i <= operands[0]; i++){
    		if (operands[i] == 0){
    			throw new ArithmeticException ("cannot divide by zero");
    		}
    	}
    	for (int i = 2; i <= operands[0]; i++){
    		total /= operands[i];
    	}
    	for (int i = 1; i < operands[0]; i++){
    		equation += (operands[i] + "/");
    	}
    	equation += operands[operands[0]] + "=" + total;
        return equation;
    }
}
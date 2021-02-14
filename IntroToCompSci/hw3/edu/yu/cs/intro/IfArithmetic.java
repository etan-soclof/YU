package edu.yu.cs.intro;
import java.util.Scanner;
public class IfArithmetic {

	public static void main (String[] args){
		Scanner scanner = new Scanner(System.in);

		//Entering operator
		System.out.print("What operation would you like to do? You can enter add, sub, mul, div\nPlease enter it now: ");
	 	String operator = scanner.next();
	 	int first;
	 	int second;
	 	double answer;
	 	boolean check = ! (operator.equals("add") || operator.equals("sub") || operator.equals("mul") || operator.equals("div"));
	 
	 	//Checking whether operator is valid
		if (check) {
		 	System.out.println("Invalid operation: " + operator);
		}

		 //If operator is valid...
		else {

		 	//Entering the first operand
		 	System.out.print("Please enter the first operand: ");
		 	first = scanner.nextInt();
		 	
		 	//Enterting the second operand
		 	System.out.print("Please enter the second operand: ");
		 	second = scanner.nextInt();

		 	//solving and printing equation
		 	if (operator.equals("add")) {
		 		answer = first+second;
		 		System.out.println(first + " + " + second + " = " + (int)answer);
		 	}

		 	else if (operator.equals("sub")){
		 		answer = first-second;
		 		System.out.println(first + " - " + second + " = " + (int)answer);
		 	}

		 	else if (operator.equals("mul")){
		 		answer = first*second;
		 		System.out.println(first + " * " + second + " = " + (int)answer);
		 	}

	 		else if (operator.equals("div")){
	 			answer = (double)first/(double)second;
	 			System.out.println(first + " / " + second + " = " + answer);
	 		}

		}

	}
	
}
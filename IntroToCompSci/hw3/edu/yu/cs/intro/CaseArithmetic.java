package edu.yu.cs.intro;
import java.util.Scanner;
public class CaseArithmetic {

	public static void main (String[] args){

		Scanner scanner = new Scanner(System.in);
		//Entering operator
		System.out.print("What operation would you like to do? You can enter add, sub, mul, div\nPlease enter it now: ");
	 	String operator = scanner.next();
	 	int first = 0;
	 	int second = 0;
	 	double answer = 0;

	 	//Checking whether operator is valid
		switch (operator){
			
			case "add" : case "sub" : case "mul" : case "div" :
				//Entering the first operand
		 		System.out.print("Please enter the first operand: ");
		 		first = scanner.nextInt();
		 	
		 		//Enterting the second operand
		 		System.out.print("Please enter the second operand: ");
		 		second = scanner.nextInt();
		 		break;
		 	
		 	default :
		 		System.out.println("Invalid operation: " + operator);
		}

		switch (operator){

			case "add" :
				answer = first+second;
		 		System.out.println(first + " + " + second + " = " + (int)answer);
		 		break;

		 	case "sub" :
		 		answer = first-second;
		 		System.out.println(first + " - " + second + " = " + (int)answer);
		 		break;

		 	case "mul" :
		 		answer = first*second;
		 		System.out.println(first + " * " + second + " = " + (int)answer);
		 		break;

		 	case "div" :
		 		answer = (double)first/(double)second;
	 			System.out.println(first + " / " + second + " = " + answer);
	 			break;
		}
	}
}
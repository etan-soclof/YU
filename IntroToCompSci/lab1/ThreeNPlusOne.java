/**
* Basic outline of the solution:
Take a number and, by dividing by two by two and multiplying by three and adding one, get to the number one. Print out all of the numbers and the amount of numbers. 

* First pseudocode refinement:
Get number from user.
Check whether the number is even or odd. 
If it is odd multiply it by 3 and add 1.
If it is even, divide by 2.
Check if the result is equal to 1. 
If it is equal to one, print out all of the numbers and the number of numbers.
It it is not equal to one, repeat this process from checking whether it is even or odd

* pseudocode of the solution, which is roughly
* 1:1 with the actual code:
Prompt user to enter positive number
Get number from user
Call method to perform the algorithm with the input as the parameter
Print the returned value
The method should:
declare a string
declare an int and set to zero
while the value is not equal to 1 do the following:
Check whether the value%2 is equal to 0.
If it is, divide by 2.
If it is not multiply it by 3 and add 1.
concactinate the number to the declared string
keep count of the amount of times the loop runs
after the loop is completed: print out the amount of times the loop ran and return the string

*
*/

import java.util.Scanner;

public class ThreeNPlusOne {

	public static void main (String [] args) {
		System.out.println("Enter a positive number");
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		System.out.println(generateThreeN(input));
	}

	public static String generateThreeN (int input){
		String numbers = "" + input;
		int count = 1;
		while (input != 1){
			if (input%2 == 0){
				input /= 2;
			}
			else {
				input = (input*3) + 1;
			}
			numbers = numbers + "," + input;
			count++;
		}
		System.out.println(count);
		return numbers;

	}
}
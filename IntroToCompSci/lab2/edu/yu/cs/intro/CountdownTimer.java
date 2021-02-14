package edu.yu.cs.intro;
import java.util.Scanner;

public class CountdownTimer {

	private long pad = 0;
	public static void main (String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.println("What two numbers would you like to time a countdown from?");
		long num1 = scanner.nextLong();
		long num2 = scanner.nextLong();
		long padLong = 0;
	
		System.out.println("Would you like to pad the time?");
		String padString = scanner.next();

		if (padString.equalsIgnoreCase("yes")){
			System.out.println("By how many miliseconds would you like to pad?");
			padLong = scanner.nextLong();
		}
		else {
			padLong = 0;
		}

		CountdownTimer countdownTimer = new CountdownTimer(padLong);

		Double value1 = countdownTimer.countdown(num1);
		System.out.println("Counting down from " + num1 + " took " + value1*1000 + " milliseconds, which is " +  value1 + " seconds");
		Double value2 = countdownTimer.countdown(num2);
		System.out.println("Counting down from " + num2 + " took " + value2*1000 + " milliseconds, which is " +  value2 + " seconds");
	}

	public CountdownTimer (long pad){
		this.pad = pad;
	}

	public double countdown (long num) throws IllegalArgumentException {
		
		if (num <= 0){
			throw new IllegalArgumentException ("I am unhappy because you entered a number that is <= 0");
		}

		if (this.pad == 0){
			Stopwatch stopwatch = new Stopwatch();
			stopwatch.start();
			while (num>0){
				num--;
			}
			stopwatch.stop();

			return stopwatch.elapsedSeconds();
		}

		else {
			Stopwatch stopwatch = new Stopwatch(this.pad);
			stopwatch.start();
			while (num>0){
				num--;
			}
			stopwatch.stop();

			return stopwatch.elapsedSeconds();
		}

	}


}
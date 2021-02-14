package edu.yu.cs.intro;

public class Rational {

	private int numerator, denominator;

	public static void main (String [] args){
		Rational defaultRational = new Rational();
		System.out.println(defaultRational.printRational());

		Rational customRational = new Rational (6, 18);
		System.out.println(customRational.printRational());

		//Testing invert
		//customRational.invert();
		//System.out.println(customRational.printRational());

		//Testing toDouble
		System.out.println(customRational.toDouble());

		//Testing GCD
		Rational gcd = customRational.reduce();
		customRational = gcd;
		System.out.println(customRational.printRational());

		Rational add1 = new Rational (18, 27);
		customRational = customRational.add(add1);
		System.out.println(customRational.printRational());

	}

	public Rational (){
		this.numerator = 0;
		this.denominator = 1;
	}

	public Rational (int numerator, int denominator) throws IllegalArgumentException {
		if (denominator ==0){
			throw new IllegalArgumentException ("Cannot divide by zero");
		}
		this.numerator = numerator;
		this.denominator = denominator;

	}

	public String printRational() {
		return "" + numerator + "/" + denominator;
	}

	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}

	public void invert () throws UnsupportedOperationException{
		int numPlaceholder;

		if (this.numerator == 0){
			throw new UnsupportedOperationException ("Cannot divide by 0");
		}

		else {
			numPlaceholder = this.numerator;
			this.numerator = this.denominator;
			this.denominator = numPlaceholder;
		}

	}

	public double toDouble(){
		return (double) this.numerator/this.denominator;
	}

	public Rational reduce (){

		int remainder = 0;
		int num1 = 0;
		int num2 = 0;

		if (this.numerator == 0){
			Rational reduced = new Rational (this.denominator, this.denominator);
			return reduced;
		}

		if (this.numerator>=this.denominator){
			remainder = this.numerator%this.denominator;
			num1 = this.denominator;
		}
		else if (this.denominator>this.numerator){
			remainder = this.denominator%this.numerator;
			num1 = this.numerator;
		}

		int gcd = remainder;

		if (remainder == 0){
			gcd = num1;
		}

		while (remainder != 0){
			num2 = num1%remainder;
			gcd = remainder;
			num1 = remainder;
			remainder = num2;
		}
		
		Rational reduced = new Rational (this.numerator/gcd, this.denominator/gcd);
		return reduced;
	}

	public Rational add(final Rational that){

		int commonDenominator = that.denominator*this.denominator;
		int addedNumerator = (that.numerator*this.denominator) + (this.numerator*that.denominator);

		Rational addedRational = new Rational (addedNumerator, commonDenominator);

		return addedRational.reduce();

	}

}
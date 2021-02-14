package edu.yu.cs.intro.bank.exceptions;

public class UnauthorizedActionException extends Exception {
	public UnauthorizedActionException (){}
	public UnauthorizedActionException (String details){
		super(details);
	}
}

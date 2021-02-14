package edu.yu.cs.intro.bank.exceptions;

public class NoSuchAccountException extends Exception {
	public NoSuchAccountException (){}
	public NoSuchAccountException (String details){
		super(details);
	}
}

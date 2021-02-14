package edu.yu.cs.intro.bank.exceptions;

public class AuthenticationException extends Exception {
	public AuthenticationException (){}
	public AuthenticationException (String details){
		super(details);
	}

}

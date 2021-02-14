package edu.yu.cs.intro.bank.exceptions;

public class InsufficientAssetsException extends Exception {
	public InsufficientAssetsException (){}
	public InsufficientAssetsException (String details){
		super(details);
	}
}

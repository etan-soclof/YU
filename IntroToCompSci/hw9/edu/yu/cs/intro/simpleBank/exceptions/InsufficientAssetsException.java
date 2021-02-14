package edu.yu.cs.intro.simpleBank.exceptions;

public class InsufficientAssetsException extends Exception {
	public InsufficientAssetsException (){}
	public InsufficientAssetsException (String details){
		super(details);
	}
}

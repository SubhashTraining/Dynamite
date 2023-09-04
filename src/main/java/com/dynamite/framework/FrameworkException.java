package com.dynamite.framework;


@SuppressWarnings("serial")
public class FrameworkException extends Exception {
	
	public  String errorName =" Error";
	
	
	public FrameworkException(String errorDescription) {
		super(errorDescription);	
		
	}
	
	/**
	 * Constructor to throw user defined Exception from the framework
	 * @param errorDescription - the description of the exception
	 * @param error - the stepName for the error
	 */
	
	public FrameworkException(String errorDescription, String error) {
		super(errorDescription);
		this.errorName =error;
	}
	
	public String getErrorName() {
		return errorName;
	}

}

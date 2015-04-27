package com.lamfire.warden;

public class ActionNotFoundException extends Exception{

	private static final long serialVersionUID = 335769517425340144L;

	public ActionNotFoundException() {
		super();
	}

	public ActionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionNotFoundException(String message) {
		super(message);
	}

	public ActionNotFoundException(Throwable cause) {
		super(cause);
	}
	
	

}

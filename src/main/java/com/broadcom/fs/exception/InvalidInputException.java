package com.broadcom.fs.exception;

public class InvalidInputException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8292432773380509421L;

	public InvalidInputException() {

	}

	public InvalidInputException(String message) {
		super(message);
	}

	public InvalidInputException(Throwable cause) {
		super(cause);
	}

	public InvalidInputException(String message, Throwable cause) {
		super(message, cause);
	}
}

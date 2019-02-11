package com.revolut.exception;

public class AccountException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	
	public AccountException() {
        this("Error while processing the request", null);
    }

    public AccountException(String message) {
        this( message, null);
    }

    public AccountException(String message, Throwable throwable) {
        super(message, throwable);
    }

	public AccountException(int errorCode, String message) {
		 super(message);
		 this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}


}
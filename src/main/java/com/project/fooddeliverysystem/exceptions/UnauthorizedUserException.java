package com.project.fooddeliverysystem.exceptions;

public class UnauthorizedUserException extends RuntimeException{
	private static final long serialVersionUID = 2315783994437555896L;
	
	private String message;
	private int errorCode;
	
	public UnauthorizedUserException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UnauthorizedUserException(String message) {
		super(message);
		this.message = message;
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedUserException(String message, int error_code) {
		super(message);
		this.message = message;
		this.errorCode = error_code;
		// TODO Auto-generated constructor stub
	}
	
	public UnauthorizedUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "UnauthorizedUserException [message=" + message + ", errorCode=" + errorCode + "]";
	}
}

package com.project.fooddeliverysystem.exceptions;

public class NotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1315783994437555895L;
	private String message;
	
	public NotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public NotFoundException(String message) {
		super(message);
		this.message = message;
		// TODO Auto-generated constructor stub
	}
	public NotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "NotFoundException [message=" + message + "]";
	}
	
}
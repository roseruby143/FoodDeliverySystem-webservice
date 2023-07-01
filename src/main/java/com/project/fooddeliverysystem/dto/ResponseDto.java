package com.project.fooddeliverysystem.dto;

import java.util.Date;

public class ResponseDto {
	
	private String status;
	private String message;
	private Date timestamp = new Date();
	private Object data;
	
	public ResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseDto(String status, String message, Date timestamp, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseDto [status=" + status + ", message=" + message + ", timestamp=" + timestamp + ", data=" + data
				+ "]";
	}
	

}

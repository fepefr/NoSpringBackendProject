package com.revolut.vo;

public class FieldError {
	private String code;
	private String message;
	private String field;
	
	public FieldError(String field2, String message2) {
		this.message = message2;
		field = field2;
	}
	public FieldError() {
		super();
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
}

package com.revolut.vo;

import java.util.ArrayList;
import java.util.List;
public class Error {

	private Integer errorCode;
      private String message;
      private List<FieldError> fieldErrors = new ArrayList<FieldError>();
      
      public static final int ERROR_CODE_INSUFFICIENT_FUNDS = 10;
      public static final int ERROR_CODE_SAME_ACCT_TRANSF = 11;
      public static final int ERROR_CODE_INVALID_PARAM = 12;
      public static final int ERROR_CODE_ACCOUNT_NOT_FOUND = 13;
      public static final String ERROR_MSG_VALIDATION_ERROR = "Parameter validation error.";
      public static final String ERROR_MSG_ACCOUNT_NOT_FOUND = "Account {0} not found.";
      public static final String ERROR_MSG_BALANCE_IS_NOT_SUFFICIENT = "Balance is not sufficient: ";
      public static final String ERROR_MSG_INVALID_TRANSFERECES_TO_THE_SAME_ACCOUNT = "Invalid transfereces to the same account.";
      
      public Error() {
    	  super();
      }
      
	public Error(Integer errorCode, String message) {
	    this.errorCode = errorCode;
	    this.message = message;
	}
      
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

    public void addFieldError(String path, String message) {
        FieldError error = new FieldError(path, message);
        fieldErrors.add(error);
    }

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
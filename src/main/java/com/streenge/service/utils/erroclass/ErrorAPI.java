package com.streenge.service.utils.erroclass;

public class ErrorAPI {
	private boolean	errorAPI	= true;
	private String	message		= "";

	public ErrorAPI() {
		// TODO Auto-generated constructor stub
	}
	public ErrorAPI(String message) {
		// TODO Auto-generated constructor stub
		this.setMessage(message);
	}
	
	public boolean isErrorAPI() {
		return errorAPI;
	}

	public void setErrorAPI(boolean errorAPI) {
		this.errorAPI = errorAPI;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

package com.streenge.service.utils.erroclass;

public class StreengeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -208712448278395818L;
	private ErrorAPI errorAPI;
	 
	public StreengeException(ErrorAPI errorAPI) {
	        //super(message);
	        this.setErrorAPI(errorAPI);
	    }

	public ErrorAPI getErrorAPI() {
		return errorAPI;
	}

	public void setErrorAPI(ErrorAPI errorAPI) {
		this.errorAPI = errorAPI;
	}
}

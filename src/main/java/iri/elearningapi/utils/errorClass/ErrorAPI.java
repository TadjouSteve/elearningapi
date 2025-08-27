package iri.elearningapi.utils.errorClass;

public class ErrorAPI {
	private boolean	errorAPI	= true;
	private String	message		= "";
	private int index=-1;

	public ErrorAPI() {
		// TODO Auto-generated constructor stub
	}
	public ErrorAPI(String message) {
		// TODO Auto-generated constructor stub
		this.setMessage(message);
	}
	
	public ErrorAPI(String message,int index) {
		// TODO Auto-generated constructor stub
		this.setMessage(message);
		this.setIndex(index);
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
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}

package iri.elearningapi.utils.errorClass;

public class ElearningException extends RuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1247132780725673014L;
	private ErrorAPI			errorAPI;

	public ElearningException(ErrorAPI errorAPI) {
		// super(message);
		this.setErrorAPI(errorAPI);
	}

	public ErrorAPI getErrorAPI() {
		return errorAPI;
	}

	public void setErrorAPI(ErrorAPI errorAPI) {
		this.errorAPI = errorAPI;
	}

}

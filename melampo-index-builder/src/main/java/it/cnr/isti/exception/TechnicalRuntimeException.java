package it.cnr.isti.exception;

public class TechnicalRuntimeException extends RuntimeException {

	public TechnicalRuntimeException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3672999785376920974L;

}

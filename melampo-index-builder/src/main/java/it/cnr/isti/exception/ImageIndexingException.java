package it.cnr.isti.exception;


public class ImageIndexingException extends Exception {

	private static final long serialVersionUID = 2853865401612458998L;

	public ImageIndexingException(String message) {
		super(message);
	}

	public ImageIndexingException(String message, Throwable th) {
		super(message, th);
	}

}

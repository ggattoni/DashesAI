package domain;

public class AlreadySignedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlreadySignedException(String string) {
		super(string);
	}

}

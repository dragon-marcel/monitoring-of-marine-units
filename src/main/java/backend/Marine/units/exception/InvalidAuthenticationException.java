package backend.Marine.units.exception;

public class InvalidAuthenticationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAuthenticationException() {
		super("Invalid username or password");
	}

}

package backend.Marine.units.exception;

public class UserUniqueException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserUniqueException(String userName) {
		super("User name " + userName + " isn't unique. Please enter another username");
	}

}

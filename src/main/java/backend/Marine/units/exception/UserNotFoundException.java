package backend.Marine.units.exception;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(int id) {
		super("User not found by id: " + id);
	}

	public UserNotFoundException(String username) {
		super("User not found by username: " + username);
	}
}

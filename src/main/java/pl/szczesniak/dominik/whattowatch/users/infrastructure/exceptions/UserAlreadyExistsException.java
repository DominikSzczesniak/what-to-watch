package pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(final String message) {
		super(message);
	}

}

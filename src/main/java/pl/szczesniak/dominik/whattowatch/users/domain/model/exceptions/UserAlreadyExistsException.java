package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
	public UserAlreadyExistsException(final String message) {
		super(message);
	}
}

package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(final String message) {
		super(message);
	}

}

package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class InvalidUsernameException extends RuntimeException {

	public InvalidUsernameException(final String message) {
		super(message);
	}

}

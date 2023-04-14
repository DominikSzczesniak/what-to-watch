package pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions;

public class UsernameIsTakenException extends RuntimeException {

	public UsernameIsTakenException(final String message) {
		super(message);
	}

}

package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class UsernameIsTakenException extends RuntimeException {
	public UsernameIsTakenException(final String message) {
		super(message);
	}
}

package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class InvalidUserIdValueException extends RuntimeException{
	public InvalidUserIdValueException(final String message) {
		super(message);
	}
}

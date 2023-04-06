package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class InvlaidUserIdException extends RuntimeException{
	public InvlaidUserIdException(final String message) {
		super(message);
	}
}

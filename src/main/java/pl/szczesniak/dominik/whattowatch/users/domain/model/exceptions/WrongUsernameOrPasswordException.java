package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class WrongUsernameOrPasswordException extends RuntimeException {

	public WrongUsernameOrPasswordException(final String message) {
		super(message);
	}

}

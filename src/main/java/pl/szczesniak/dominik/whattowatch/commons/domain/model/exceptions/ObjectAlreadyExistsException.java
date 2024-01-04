package pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions;

public class ObjectAlreadyExistsException extends RuntimeException {

	public ObjectAlreadyExistsException(final String message) {
		super(message);
	}

}

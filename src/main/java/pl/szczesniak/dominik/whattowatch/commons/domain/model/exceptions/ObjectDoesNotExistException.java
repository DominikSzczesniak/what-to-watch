package pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions;

public class ObjectDoesNotExistException extends RuntimeException {

	public ObjectDoesNotExistException(final String message) {
		super(message);
	}

}

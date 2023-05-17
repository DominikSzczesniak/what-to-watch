package pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions;

public class ObjectDoesNotExistException extends RuntimeException {

	public ObjectDoesNotExistException(final String message) {
		super(message);
	}

}

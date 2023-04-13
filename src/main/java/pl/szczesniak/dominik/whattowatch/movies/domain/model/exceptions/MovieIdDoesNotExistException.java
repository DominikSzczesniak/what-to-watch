package pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions;

public class MovieIdDoesNotExistException extends RuntimeException {

	public MovieIdDoesNotExistException(final String message) {
		super(message);
	}

}

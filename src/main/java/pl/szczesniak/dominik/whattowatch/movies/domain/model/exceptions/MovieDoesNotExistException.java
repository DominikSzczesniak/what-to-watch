package pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions;

public class MovieDoesNotExistException extends RuntimeException {

	public MovieDoesNotExistException(final String message) {
		super(message);
	}

}

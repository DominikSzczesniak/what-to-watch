package pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions;

public class MovieDoestNotBelongToUserException extends RuntimeException {

	public MovieDoestNotBelongToUserException(final String message) {
		super(message);
	}

}

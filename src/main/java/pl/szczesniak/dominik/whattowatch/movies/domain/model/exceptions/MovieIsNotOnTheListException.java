package pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions;

public class MovieIsNotOnTheListException extends RuntimeException {

    public MovieIsNotOnTheListException(final String message) {
        super(message);
    }
}

package pl.szczesniak.dominik.videos.domain.exceptions;

public class MovieIsNotOnTheListException extends RuntimeException {

    public MovieIsNotOnTheListException(final String message) {
        super(message);
    }
}

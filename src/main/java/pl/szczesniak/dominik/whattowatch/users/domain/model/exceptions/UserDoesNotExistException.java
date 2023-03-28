package pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(final String message) {
        super(message);
    }
}

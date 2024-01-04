package pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.incoming.rest;

public class BadRequestException extends RuntimeException {

	public BadRequestException(final String message) {
		super(message);
	}

}

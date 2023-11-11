package pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.incoming.rest;

import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionsHandler {

	@ExceptionHandler(ObjectDoesNotExistException.class)
	public ResponseEntity<Object> handleObjectDoesNotExistException(final ObjectDoesNotExistException e) {
		final ErrorResponse response = new ErrorResponse(LocalDateTime.now(), e.getMessage(), 404, "ObjectDoesNotExistException");
		return ResponseEntity.status(404).body(response);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(final BadRequestException e) {
		final ErrorResponse response = new ErrorResponse(LocalDateTime.now(), e.getMessage(), 404, "BadRequestException");
		return ResponseEntity.status(404).body(response);
	}

	@Value
	private static class ErrorResponse {

		LocalDateTime timestamp;
		String message;
		int status;
		String error;

	}

}

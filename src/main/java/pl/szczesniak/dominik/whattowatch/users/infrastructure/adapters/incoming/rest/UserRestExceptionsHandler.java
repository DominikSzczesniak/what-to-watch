package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;

@ControllerAdvice
public class UserRestExceptionsHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException() {
		return new ResponseEntity<>("User already exists.", HttpStatusCode.valueOf(400));
	}

}

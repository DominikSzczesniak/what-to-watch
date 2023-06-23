package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

@RequiredArgsConstructor
@RestController
public class UserRestController {

	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	private final UserService userService;

	@PostMapping("/api/login")
	public ResponseEntity<Integer> loginUser(@RequestBody final LoginUserDto userDto) {
		try {
			final Integer userId = userService.login(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword())).getValue();
			return ResponseEntity.status(HttpStatus.OK).body(userId);
		} catch (InvalidCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Integer> createUser(@RequestBody final CreateUserDto userDto) {
		boolean isUsernameTaken = checkUsernameAvailability(new Username(userDto.getUsername()));
		if (isUsernameTaken) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		final Integer userId = userService.createUser(new CreateUser(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword()))).getValue();
		return ResponseEntity.status(HttpStatus.CREATED).body(userId);
	}

	private boolean checkUsernameAvailability(final Username username) {
		return userService.isUsernameTaken(username);
	}

	@ExceptionHandler(UsernameIsTakenException.class)
	public ResponseEntity<?> handleUsernameIsTakenException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<?> handleInvalidCredentialsException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(PSQLException.class)
	public ResponseEntity<?> handlePSQLException(final PSQLException ex) {
		logger.error("An exception occurred while handling PSQLException", ex);
		return ResponseEntity.internalServerError().build();
	}

	@Data
	private static class CreateUserDto {
		private String username;
		private String password;
	}

	@Value
	private static class LoginUserDto {
		String username;
		String password;
	}

}

package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

@RequiredArgsConstructor
@RestController
public class UserRestController {

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
		try {
			final Integer value = userService.createUser(new CreateUser(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword()))).getValue();
			return ResponseEntity.status(HttpStatus.CREATED).body(value);
		} catch (UsernameIsTakenException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/api/users/{username}")
	public ResponseEntity<String> isUsernameTaken(@PathVariable final String username) {
		boolean check = userService.isUsernameTaken(new Username(username));
		return ResponseEntity.status(HttpStatus.OK).body("username is taken: " + check);
	}

	@ExceptionHandler(UsernameIsTakenException.class)
	public ResponseEntity<?> handleUsernameIsTakenException() {
		return ResponseEntity.badRequest().build();
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

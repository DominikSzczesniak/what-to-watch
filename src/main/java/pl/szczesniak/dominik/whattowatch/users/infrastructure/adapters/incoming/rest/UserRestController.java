package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions.UsernameIsTakenException;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserRestController {

	private final UserService userService;

	@GetMapping("/api/user")
	public UserId loginUser(@RequestBody final CreateUserDto userDto) {
		return userService.login(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword()));
	}

	@PostMapping("/api/user")
	public ResponseEntity<?> createUser(@RequestBody final CreateUserDto userDto) {
		userService.createUser(new CreateUser(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword())));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@ExceptionHandler(UsernameIsTakenException.class)
	public ResponseEntity<?> handleUsernameIsTakenException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<?> handleInvalidCredentialsException() {
		return ResponseEntity.badRequest().build();
	}

	@Data
	private static class CreateUserDto {
		private String username;
		private String password;
	}

}
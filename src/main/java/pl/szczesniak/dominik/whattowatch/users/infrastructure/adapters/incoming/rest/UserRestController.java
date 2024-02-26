package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.users.domain.UserFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

@RequiredArgsConstructor
@RestController
public class UserRestController {

	private final UserFacade userFacade;

	private final AuthenticationManager authenticationManager;
	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	@PostMapping("/api/login")
	public ResponseEntity<Integer> loginUser(@RequestBody final LoginUserDto userDto, final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final Integer userId = userFacade.login(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword())).getValue();
			login(userDto, request, response);
			return ResponseEntity.status(HttpStatus.OK).body(userId);
		} catch (InvalidCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	private void login(final LoginUserDto userDto, final HttpServletRequest request, final HttpServletResponse response) {
		final UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
				userDto.getUsername(), userDto.getPassword());
		final Authentication authentication = authenticationManager.authenticate(token);
		final SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		securityContextRepository.saveContext(context, request, response);
	}

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Integer> createUser(@RequestBody final CreateUserDto userDto) {
		try {
			final Integer value = userFacade.createUser(new CreateUser(new Username(userDto.getUsername()), new UserPassword(userDto.getPassword()))).getValue();
			return ResponseEntity.status(HttpStatus.CREATED).body(value);
		} catch (UsernameIsTakenException | DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/api/users/{username}")
	public ResponseEntity<String> isUsernameTaken(@PathVariable final String username) {
		boolean check = userFacade.isUsernameTaken(new Username(username));
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

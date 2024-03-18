package pl.szczesniak.dominik.whattowatch.security;

import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.CreateUserRestInvoker;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.CreateUserRestInvoker.CreateUserDto;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.LoginUserRestInvoker;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.LoginUserRestInvoker.LoginUserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@Component
public class LoggedUserProvider {

	@Autowired
	private CreateUserRestInvoker createUserRest;

	@Autowired
	private LoginUserRestInvoker loginUserRest;

	public LoggedUser getLoggedUser() {
		final CreateUserDto build = CreateUserDto.builder()
				.username(createAnyUsername().getValue()).password(createAnyUserPassword().getValue()).build();
		final ResponseEntity<Integer> createUserResponse = createUserRest.createUser(build, Integer.class);
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);


		final ResponseEntity<Integer> loggedUserResponse = loginUserRest.loginUser(
				LoginUserDto.builder().username(build.getUsername()).password(build.getPassword()).build(),
				Integer.class
		);
		assertThat(loggedUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		final List<String> cookies = loggedUserResponse
				.getHeaders()
				.get("Set-Cookie");
		final Integer userId = loggedUserResponse.getBody();

		return new LoggedUser(userId, cookies);
	}

	@Value
	public static class LoggedUser { // todo: usunac to chyba

		@NonNull Integer userId;
		@NonNull List<String> sessionId;

	}
}

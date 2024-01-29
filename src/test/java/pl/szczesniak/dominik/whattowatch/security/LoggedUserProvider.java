package pl.szczesniak.dominik.whattowatch.security;

import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
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
		final ResponseEntity<Integer> user = createUserRest.createUser(build, Integer.class);
		assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CREATED);


		final ResponseEntity<Integer> loggedUserResponse = loginUserRest.loginUser(
				LoginUserDto.builder().username(build.getUsername()).password(build.getPassword()).build(),
				Integer.class
		);

		final List<String> cookies = loggedUserResponse
				.getHeaders()
				.get("Set-Cookie");
		final Integer userId = loggedUserResponse.getBody();
		checkUserIdOrCookiesAreNotNull(cookies, userId);

		return new LoggedUser(userId, cookies);
	}

	private static void checkUserIdOrCookiesAreNotNull(final List<String> cookies, final Integer userId) {
		if (userId == null) {
			throw new ObjectDoesNotExistException("userid is null");
		}
		if (cookies == null) {
			throw new ObjectDoesNotExistException("cookies not found");
		}
	}

	@Value
	public static class LoggedUser {

		@NonNull Integer userId;
		@NonNull List<String> sessionId;

	}
}

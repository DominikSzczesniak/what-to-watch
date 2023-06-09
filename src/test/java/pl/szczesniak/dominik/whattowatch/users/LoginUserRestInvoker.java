package pl.szczesniak.dominik.whattowatch.users;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@Component
@RequiredArgsConstructor
public class LoginUserRestInvoker {

	private static final String URL = "/api/login";

	private final TestRestTemplate restTemplate;

	public <T> ResponseEntity<T> loginUser(final LoginUserDto loginUserDto, final Class<T> responseType) {
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(loginUserDto),
				responseType
		);
	}

	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class LoginUserDto {

		String username;
		String password;

		@Builder
		private static LoginUserDto build(final String username, final String password) {
			return new LoginUserDto(
					ofNullable(username).orElse(createAnyUsername().getValue()),
					ofNullable(password).orElse(createAnyUserPassword().getValue())
			);
		}

	}

}

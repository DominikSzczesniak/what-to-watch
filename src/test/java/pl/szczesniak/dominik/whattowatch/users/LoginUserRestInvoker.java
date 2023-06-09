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
	@Builder
	public static class LoginUserDto {

		String username;
		String password;

	}

}

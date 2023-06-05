package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.Value;
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

	@Value
	public static class LoginUserDto {
		String username;
		String password;
	}

}

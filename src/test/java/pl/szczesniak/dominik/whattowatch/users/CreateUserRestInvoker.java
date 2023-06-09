package pl.szczesniak.dominik.whattowatch.users;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserRestInvoker {

	private static final String URL = "/api/users";

	private final TestRestTemplate restTemplate;

	public <T> ResponseEntity<T> createUser(final CreateUserDto createUserDto, final Class<T> responseType) {
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(createUserDto),
				responseType
		);
	}

	@Data
	@Builder
	public static class CreateUserDto {
		private String username;
		private String password;

	}

}

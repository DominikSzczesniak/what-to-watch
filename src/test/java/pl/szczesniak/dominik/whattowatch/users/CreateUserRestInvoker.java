package pl.szczesniak.dominik.whattowatch.users;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

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

	public static CreateUserDto createAnyUser() {
		return new CreateUserDto(createAnyUsername().getValue(), createAnyUserPassword().getValue());
	}

	@Data
	@Builder
	public static class CreateUserDto {

		private final String username;
		private final String password;

	}

}

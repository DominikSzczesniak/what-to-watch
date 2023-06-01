package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;

import lombok.Data;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserRestControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void should_create_user_and_login_on_him() {
		// when
		final String username = createAnyUsername().getValue();
		final String password = createAnyUserPassword().getValue();

		final ResponseEntity<Void> createUserResponse = restTemplate.exchange(
				"/api/users",
				HttpMethod.POST,
				new HttpEntity<>(createUser(username, password)),
				Void.class
		);

		// then
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		final ResponseEntity<UserId> loginUserResponse = restTemplate.exchange(
				"/api/login",
				HttpMethod.POST,
				new HttpEntity<>(new LoginUserDto(username, password)),
				UserId.class
		);
		assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	private static Map<String, String> createUser(final String username, final String password) {
		return Map.of(
				"username", username,
				"password", password
		);
	}

	@Data
	private static class CreateUserDto {
		String username;
		String password;
	}

	@Value
	private static class LoginUserDto {
		String username;
		String password;
	}

}
package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;

import lombok.Value;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.CreateUserRestInvoker.CreateUserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("memory")
class UserRestControllerIntegrationTest {

	@Autowired
	CreateUserRestInvoker createUserRest;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void should_create_user_and_login_on_him() {
		// given
		final CreateUserDto userToCreate = createAnyUser();

		// when
		final ResponseEntity<UserId> createUserResponse = createUserRest.createUser(userToCreate, UserId.class);

		// then
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<UserId> loginUserResponse = restTemplate.exchange(
				"/api/login",
				HttpMethod.POST,
				new HttpEntity<>(new LoginUserDto(userToCreate.getUsername(), userToCreate.getPassword())),
				UserId.class
		);

		// then
		assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(loginUserResponse.getBody()).isNotNull();
		assertThat(loginUserResponse.getBody().getValue()).isGreaterThan(0);
	}

	private static CreateUserDto createAnyUser() {
		return new CreateUserDto(createAnyUsername().getValue(), createAnyUserPassword().getValue());
	}

	@Value
	private static class LoginUserDto {
		String username;
		String password;
	}

}
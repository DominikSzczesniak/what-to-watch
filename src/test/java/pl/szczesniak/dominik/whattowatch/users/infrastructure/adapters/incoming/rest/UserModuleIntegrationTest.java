package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.CreateUserRestInvoker.CreateUserDto;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.LoginUserRestInvoker.LoginUserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("memory")
class UserModuleIntegrationTest {

	@Autowired
	private CreateUserRestInvoker createUserRest;

	@Autowired
	private LoginUserRestInvoker loginUserRest;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void should_create_user_and_login_on_him() {
		// given
		final CreateUserDto userToCreate = createAnyUser();

		// when
		final ResponseEntity<Integer> createUserResponse = createUserRest.createUser(userToCreate, Integer.class);

		// then
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<Integer> loginUserResponse = loginUserRest.loginUser(new LoginUserDto(userToCreate.getUsername(),
				userToCreate.getPassword()),
				Integer.class
		);

		// then
		assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(loginUserResponse.getBody()).isNotNull();
		assertThat(loginUserResponse.getBody()).isEqualTo(createUserResponse.getBody());
	}

	@Test
	void should_create_user_and_not_login() {
		// given
		final CreateUserDto userToCreate = createAnyUser();

		// when
		final ResponseEntity<Integer> createUserResponse = createUserRest.createUser(userToCreate, Integer.class);

		// then
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<Integer> loginUserResponse = loginUserRest.loginUser(new LoginUserDto(
				userToCreate.getUsername(),
				createAnyUserPassword().getValue()),
				Integer.class
		);

		// then
		assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void should_not_create_user_with_same_username() {
		// given
		final CreateUserDto userToCreate = createAnyUser();

		// when
		final ResponseEntity<Integer> createUserResponse = createUserRest.createUser(userToCreate, Integer.class);

		// then
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		ResponseEntity<Integer> duplicatedUsernameResponse = createUserRest.createUser(
				new CreateUserDto(userToCreate.getUsername(),
						createAnyUserPassword().getValue()),
				Integer.class
		);

		// then
		assertThat(duplicatedUsernameResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private static CreateUserDto createAnyUser() {
		return new CreateUserDto(createAnyUsername().getValue(), createAnyUserPassword().getValue());
	}

}
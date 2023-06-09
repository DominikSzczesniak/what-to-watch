package pl.szczesniak.dominik.whattowatch.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.CreateUserRestInvoker;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.CreateUserRestInvoker.CreateUserDto;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.LoginUserRestInvoker;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.incoming.rest.LoginUserRestInvoker.LoginUserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserModuleIntegrationTest {

	@Autowired
	private CreateUserRestInvoker createUserRest;

	@Autowired
	private LoginUserRestInvoker loginUserRest;

	@Test
	void should_create_user_and_login_on_him() {
		// given
		final CreateUserDto userToCreate = createAnyUser();

		// when
		final ResponseEntity<Integer> createUserResponse = createUserRest.createUser(userToCreate, Integer.class);

		// then
		assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<Integer> loginUserResponse = loginUserRest.loginUser(
				LoginUserDto.builder().username(userToCreate.getUsername()).password(userToCreate.getPassword()).build(),
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
		final ResponseEntity<Integer> loginUserResponse = loginUserRest.loginUser(
				LoginUserDto.builder().username(userToCreate.getUsername()).password(createAnyUserPassword().getValue()).build(),
				Integer.class
		);

		// then
		assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
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
		final Throwable thrown = catchThrowable(() -> createUserRest.createUser(
				CreateUserDto.builder().username(userToCreate.getUsername()).password(createAnyUserPassword().getValue()).build(),
				Integer.class
		));

		// then
		assertThat(thrown).isInstanceOf(RestClientException.class);
	}

	private static CreateUserDto createAnyUser() {
		return CreateUserDto.builder().username(createAnyUsername().getValue()).password(createAnyUserPassword().getValue()).build();
	}

}
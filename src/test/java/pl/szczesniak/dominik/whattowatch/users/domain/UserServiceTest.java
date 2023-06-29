package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUserSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.TestUserServiceConfiguration.userService;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

class UserServiceTest {

	private UserService tut;

	@BeforeEach
	void setUp() {
		tut = userService();
	}

	@Test
	void user_should_not_exist_when_previously_not_created() {
		// given
		final UserId nonExistingUserId = createAnyUserId();

		// when
		final boolean checkExist = tut.exists(nonExistingUserId);

		// then
		assertThat(checkExist).isFalse();
	}

	@Test
	void user_should_exist_when_previously_created() {
		// given
		final long createdUserId = tut.createUser(CreateUserSample.builder().build());

		// when
		final boolean checkExist = tut.exists(new UserId(createdUserId));

		// then
		assertThat(checkExist).isTrue();
	}

	@Test
	void created_users_should_have_different_ids() {
		// when
		final Long userOne = tut.createUser(CreateUserSample.builder().build());
		final Long userTwo = tut.createUser(CreateUserSample.builder().build());

		// then
		assertThat(userOne).isNotEqualTo(userTwo);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// given
		final Long userOne = tut.createUser(CreateUserSample.builder().build());

		// when
		final Long userTwo = tut.createUser(CreateUserSample.builder().build());
		final Long userThree = tut.createUser(CreateUserSample.builder().build());
		final Long userFour = tut.createUser(CreateUserSample.builder().build());
		final Long userFive = tut.createUser(CreateUserSample.builder().build());

		// then
		assertThat(userTwo).isEqualTo(userOne + 1);
		assertThat(userThree).isEqualTo(userOne + 2);
		assertThat(userFour).isEqualTo(userOne + 3);
		assertThat(userFive).isEqualTo(userOne + 4);
	}

	@Test
	void should_not_be_able_to_create_user_with_same_username() {
		// given
		final Username username = createAnyUsername();
		tut.createUser(CreateUserSample.builder().username(username).build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser(CreateUserSample.builder().username(username).build()));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void should_login_user_when_credentials_are_correct() {
		// given
		final Username createdUsername = createAnyUsername();
		final UserPassword createdPassword = createAnyUserPassword();
		final Long createdUserId = tut.createUser(CreateUserSample.builder().username(createdUsername).userPassword(createdPassword).build());

		// when
		final Long loggedUserId = tut.login(createdUsername, createdPassword);

		// then
		assertThat(loggedUserId).isEqualTo(createdUserId);
	}

	@Test
	void should_throw_exception_when_given_wrong_username() {
		// given
		final UserPassword password = createAnyUserPassword();
		final Username username = new Username("Dominik");
		final Username wrongUsername = new Username("asd");

		tut.createUser(CreateUserSample.builder().username(username).userPassword(password).build());

		// when
		final Throwable differentUsername = catchThrowable(() -> tut.login(wrongUsername, password));

		// then
		assertThat(differentUsername).isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	void should_throw_exception_when_given_wrong_password() {
		// given
		final Username username = createAnyUsername();
		final UserPassword password = new UserPassword("password");
		final UserPassword wrongPassword = new UserPassword("asd");

		tut.createUser(CreateUserSample.builder().username(username).userPassword(password).build());

		// when
		final Throwable differentPassword = catchThrowable(() -> tut.login(username, wrongPassword));

		// then
		assertThat(differentPassword).isInstanceOf(InvalidCredentialsException.class);
	}

}
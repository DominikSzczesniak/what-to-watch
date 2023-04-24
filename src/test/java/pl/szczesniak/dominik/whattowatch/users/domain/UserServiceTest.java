package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.WrongUsernameOrPasswordException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions.UsernameIsTakenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.TestUserServiceConfiguration.userService;

class UserServiceTest {

	private UserService tut;

	@BeforeEach
	void setUp() {
		tut = userService();
	}

	@Test
	void user_should_not_exist_when_previously_not_created() {
		// given
		final UserId nonExistingUserId = new UserId(1);

		// when
		final boolean checkExist = tut.exists(nonExistingUserId);

		// then
		assertThat(checkExist).isFalse();
	}

	@Test
	void user_should_exist_when_previously_created() {
		// given
		final UserId createdUserId = tut.createUser(anyUsername(), anyUserPassword());

		// when
		final boolean checkExist = tut.exists(createdUserId);

		// then
		assertThat(checkExist).isTrue();
	}

	@Test
	void created_users_should_have_different_ids() {
		// when
		final UserId dominikId = tut.createUser(new Username("Dominik"), anyUserPassword());
		final UserId patrykId = tut.createUser(new Username("Patryk"), anyUserPassword());

		// then
		assertThat(dominikId).isNotEqualTo(patrykId);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// given
		final UserId kamilId = tut.createUser(new Username("Kamil"), anyUserPassword());

		// when
		final UserId dominikId = tut.createUser(new Username("Dominik"), anyUserPassword());
		final UserId grzegorzId = tut.createUser(new Username("Grzegorz"), anyUserPassword());
		final UserId michalId = tut.createUser(new Username("Michal"), anyUserPassword());
		final UserId patrykId = tut.createUser(new Username("Patryk"), anyUserPassword());

		// then
		assertThat(dominikId.getValue()).isEqualTo(kamilId.getValue() + 1);
		assertThat(grzegorzId.getValue()).isEqualTo(kamilId.getValue() + 2);
		assertThat(michalId.getValue()).isEqualTo(kamilId.getValue() + 3);
		assertThat(patrykId.getValue()).isEqualTo(kamilId.getValue() + 4);
	}

	@Test
	void should_not_be_able_to_create_user_with_same_username() {
		// given
		final Username username = anyUsername();
		tut.createUser(username, anyUserPassword());

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser(username, anyUserPassword()));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void should_login_user_when_credentials_are_correct() {
		// given
		final Username createdUsername = new Username("Dominik");
		final UserId createdUserId = tut.createUser(createdUsername, new UserPassword("password"));

		// when
		final UserId loggedUserId = tut.login(createdUsername, new UserPassword("password"));

		// then
		assertThat(loggedUserId).isEqualTo(createdUserId);
	}

	@Test
	void should_throw_exception_when_given_wrong_username() {
		// given
		tut.createUser(new Username("Dominik"), anyUserPassword());

		// when
		final Throwable differentUsername = catchThrowable(() -> tut.login(new Username("Kamil"), anyUserPassword()));

		// then
		assertThat(differentUsername).isInstanceOf(WrongUsernameOrPasswordException.class);
	}

	@Test
	void should_throw_exception_when_given_wrong_password() {
		// given
		tut.createUser(anyUsername(), new UserPassword("password"));

		// when
		final Throwable differentPassword = catchThrowable(() -> tut.login(anyUsername(), new UserPassword("wrong")));

		// then
		assertThat(differentPassword).isInstanceOf(WrongUsernameOrPasswordException.class);
	}

	private static Username anyUsername() {
		return new Username("Marian");
	}

	private static UserPassword anyUserPassword() {
		return new UserPassword("asd");
	}

}
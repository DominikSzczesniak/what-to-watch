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

	private final UserPassword userPassword = new UserPassword("asd");
	private UserService tut;

	@BeforeEach
	void setUp() {
		tut = userService();
	}

	@Test
	void created_users_should_have_different_ids() {
		// when
		UserId dominikId = tut.createUser(new Username("Dominik"), userPassword);
		UserId patrykId = tut.createUser(new Username("Patryk"), userPassword);

		// then
		assertThat(dominikId).isNotEqualTo(patrykId);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// when
		final UserId kamilId = tut.createUser(new Username("Kamil"), userPassword);
		final UserId dominikId = tut.createUser(new Username("Dominik"), userPassword);
		final UserId grzegorzId = tut.createUser(new Username("Grzegorz"), userPassword);
		final UserId michalId = tut.createUser(new Username("Michal"), userPassword);
		final UserId patrykId = tut.createUser(new Username("Patryk"), userPassword);

		// then
		assertThat(kamilId.getValue()).isEqualTo(1);
		assertThat(dominikId.getValue()).isEqualTo(2);
		assertThat(grzegorzId.getValue()).isEqualTo(3);
		assertThat(michalId.getValue()).isEqualTo(4);
		assertThat(patrykId.getValue()).isEqualTo(5);
	}

	@Test
	void shouldnt_be_able_to_create_user_with_same_username() {
		// given
		tut.createUser(new Username("Dominik"), userPassword);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser(new Username("Dominik"), userPassword));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void should_return_correct_userid() {
		// when
		final UserId dominik = tut.createUser(new Username("Dominik"), userPassword);
		final UserId patryk = tut.createUser(new Username("Patryk"), userPassword);
		final UserId michal = tut.createUser(new Username("Michal"), userPassword);

		// then
		assertThat(tut.login(new Username("Dominik"), userPassword)).isEqualTo(dominik);
		assertThat(tut.login(new Username("Patryk"), userPassword)).isEqualTo(patryk);
		assertThat(tut.login(new Username("Michal"), userPassword)).isEqualTo(michal);
	}

	@Test
	void should_login_user_when_credentials_are_correct() {
		// given
		final UserId createdUserId = tut.createUser(new Username("Dominik"), new UserPassword("password"));

		// when
		final UserId loggedUserId = tut.login(new Username("Dominik"), new UserPassword("password"));

		// then
		assertThat(loggedUserId).isEqualTo(createdUserId);
	}

	@Test
	void should_throw_exception_when_given_wrong_username_or_password() {
		// given
		tut.createUser(new Username("Dominik"), new UserPassword("password"));

		// when
		final Throwable differentUsername = catchThrowable(() -> tut.login(new Username("Kamil"), new UserPassword("password")));
		final Throwable differentPassword = catchThrowable(() -> tut.login(new Username("Dominik"), new UserPassword("wrong")));

		// then
		assertThat(differentUsername).isInstanceOf(WrongUsernameOrPasswordException.class);
		assertThat(differentPassword).isInstanceOf(WrongUsernameOrPasswordException.class);
	}

}
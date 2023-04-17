package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserNotFoundException;
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
		// when
		final boolean checkExist = tut.exists(new UserId(1));

		// then
		assertThat(checkExist).isFalse();
	}

	@Test
	void user_should_exist_when_previously_created() {
		// given
		final UserId dominik = tut.createUser("Dominik");

		// when
		final boolean checkExist = tut.exists(dominik);

		// then
		assertThat(checkExist).isTrue();
	}

	@Test
	void created_users_should_have_different_ids() {
		// when
		final UserId dominikId = tut.createUser("Dominik");
		final UserId patrykId = tut.createUser("Patryk");

		// then
		assertThat(dominikId).isNotEqualTo(patrykId);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// when
		final UserId kamilId = tut.createUser("Kamil");
		final UserId dominikId = tut.createUser("Dominik");
		final UserId grzegorzId = tut.createUser("Grzegorz");
		final UserId michalId = tut.createUser("Michal");
		final UserId patrykId = tut.createUser("Patryk");

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
		tut.createUser("Dominik");

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser("Dominik"));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void should_return_correct_userid() {
		// when
		final UserId dominik = tut.createUser("Dominik");
		final UserId patryk = tut.createUser("Patryk");
		final UserId michal = tut.createUser("Michal");

		// then
		assertThat(tut.login("Dominik")).isEqualTo(dominik);
		assertThat(tut.login("Patryk")).isEqualTo(patryk);
		assertThat(tut.login("Michal")).isEqualTo(michal);
	}

}
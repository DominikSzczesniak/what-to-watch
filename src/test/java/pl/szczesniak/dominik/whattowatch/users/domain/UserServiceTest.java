package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
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
		final UserId createdUserId = tut.createUser(anyUsername());

		// when
		final boolean checkExist = tut.exists(createdUserId);

		// then
		assertThat(checkExist).isTrue();
	}

	@Test
	void created_users_should_have_different_ids() {
		// when
		final UserId dominikId = tut.createUser(new Username("Dominik"));
		final UserId patrykId = tut.createUser(new Username("Patryk"));

		// then
		assertThat(dominikId).isNotEqualTo(patrykId);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// given
		final UserId kamilId = tut.createUser(new Username("Kamil"));

		// when
		final UserId dominikId = tut.createUser(new Username("Dominik"));
		final UserId grzegorzId = tut.createUser(new Username("Grzegorz"));
		final UserId michalId = tut.createUser(new Username("Michal"));
		final UserId patrykId = tut.createUser(new Username("Patryk"));

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
		tut.createUser(username);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser(username));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void should_return_correct_userid() {
		// when
		final UserId dominik = tut.createUser(new Username("Dominik"));
		final UserId patryk = tut.createUser(new Username("Patryk"));
		final UserId michal = tut.createUser(new Username("Michal"));

		// then
		assertThat(tut.login("Dominik")).isEqualTo(dominik);
		assertThat(tut.login("Patryk")).isEqualTo(patryk);
		assertThat(tut.login("Michal")).isEqualTo(michal);
	}

	private static Username anyUsername() {
		return new Username("Dominik");
	}

}
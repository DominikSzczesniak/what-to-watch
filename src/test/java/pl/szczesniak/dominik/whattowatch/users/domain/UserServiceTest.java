package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InMemoryUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.TestUserServiceConfiguration.userService;

class UserServiceTest {

	private UserService tut;

	@BeforeEach
	void setUp() {
		tut = userService(new InMemoryUserRepository());
	}

	@Test
	void user_should_exist_after_being_created() {
		// when
		final UserId id = tut.createUser(new User("Dominik", tut.nextUserId()));

		// then
		assertThat(tut.exists(id)).isTrue();
	}

	@Test
	void created_users_should_have_different_ids() {
		// given
		final User dominik = new User("Dominik", tut.nextUserId());
		final User patryk = new User("Patryk", tut.nextUserId());

		// when
		final UserId idOne = tut.createUser(dominik);
		final UserId idTwo = tut.createUser(patryk);

		// then
		assertThat(idOne.getValue()).isNotEqualTo(idTwo.getValue());
	}

	@Test
	void shouldnt_be_able_to_create_user_with_same_username() {
		// given
		final User dominik = new User("Dominik", tut.nextUserId());
		tut.createUser(dominik);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser(new User("Dominik", tut.nextUserId())));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// when
		final User kamil = new User("Kamil", tut.nextUserId());
		final User dominik = new User("dominik", tut.nextUserId());
		final User grzegorz = new User("grzegorz", tut.nextUserId());
		final User michal = new User("michal", tut.nextUserId());
		final User patryk = new User("patryk", tut.nextUserId());

		// then
		assertThat(kamil.getUserId().getValue()).isEqualTo(1);
		assertThat(dominik.getUserId().getValue()).isEqualTo(2);
		assertThat(grzegorz.getUserId().getValue()).isEqualTo(3);
		assertThat(michal.getUserId().getValue()).isEqualTo(4);
		assertThat(patryk.getUserId().getValue()).isEqualTo(5);
	}

	@Test
	void should_find_all_added_users() {
		// when
		tut.createUser(new User("Dominik", tut.nextUserId()));
		tut.createUser(new User("Kamil", tut.nextUserId()));
		tut.createUser(new User("Michal", tut.nextUserId()));
		tut.createUser(new User("Patryk", tut.nextUserId()));
		tut.createUser(new User("Grzegorz", tut.nextUserId()));

		// then
		assertThat(tut.findAllUsers()).extracting(User::getUserName).containsExactlyInAnyOrder(
				"Dominik", "Kamil", "Michal", "Patryk", "Grzegorz");
		assertThat(tut.findAllUsers()).extracting(User::getUserId).containsExactlyInAnyOrder(
				new UserId(1),
				new UserId(2),
				new UserId(3),
				new UserId(4),
				new UserId(5)
		);
	}

	@Test
	void should_throw_exception_when_creating_already_existing_user() {
		// given
		tut.createUser(new User("Dominik", new UserId(1)));

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser(new User("Patryk", new UserId(1))));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}
}
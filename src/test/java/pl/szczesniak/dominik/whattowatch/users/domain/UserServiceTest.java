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
		// given
		final User dominik = tut.createUser("Dominik");

		// when
		tut.saveUser(dominik);

		// then
		assertThat(tut.exists(dominik.getUserId())).isTrue();
	}

	@Test
	void created_users_should_have_different_ids() {
		// when
		final User dominik = tut.createUser("Dominik");
		final User patryk = tut.createUser("Dominik");

		// then
		assertThat(dominik.getUserId()).isNotEqualTo(patryk.getUserId());
	}

	@Test
	void shouldnt_be_able_to_create_user_with_same_username() {
		// given
		final User dominik = tut.createUser("Dominik");
		tut.saveUser(dominik);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createUser("Dominik"));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

	@Test
	void every_next_user_has_id_higher_by_one() {
		// when
		final User kamil = tut.createUser("Kamil");
		final User dominik = tut.createUser("Dominik");
		final User grzegorz = tut.createUser("Grzegorz");
		final User michal = tut.createUser("Michal");
		final User patryk = tut.createUser("Dominik");

		// then
		assertThat(kamil.getUserId().getValue()).isEqualTo(1);
		assertThat(dominik.getUserId().getValue()).isEqualTo(2);
		assertThat(grzegorz.getUserId().getValue()).isEqualTo(3);
		assertThat(michal.getUserId().getValue()).isEqualTo(4);
		assertThat(patryk.getUserId().getValue()).isEqualTo(5);
	}


	@Test
	void should_throw_exception_when_creating_user_with_already_existing_userid() {
		// given
		User dominik = tut.createUser("Dominik");
		tut.saveUser(dominik);
		User existing = new User("Patryk", new UserId(1));

		// when
		final Throwable thrown = catchThrowable(() -> tut.saveUser(existing));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}
}
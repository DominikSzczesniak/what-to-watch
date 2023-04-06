package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvlaidUserIdException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UserTest {

	@Test
	void should_throw_exception_when_null_username() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(null, new UserId(1)));

		// then
		assertThat(thrown).isInstanceOf(InvalidUsernameException.class);
	}

	@Test
	void should_throw_exception_when_null_userId() {
		// when
		final Throwable thrown = catchThrowable(() -> new User("dominik", null));

		// then
		assertThat(thrown).isInstanceOf(InvlaidUserIdException.class);
	}
}
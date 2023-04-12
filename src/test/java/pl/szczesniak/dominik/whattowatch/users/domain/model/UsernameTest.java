package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UsernameTest {

	@Test
	void should_throw_exception_when_invalid_username() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(new Username("123"), new UserId(1)));

		// then
		assertThat(thrown).isInstanceOf(InvalidUsernameException.class);
	}

}
package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

class UserTest {

	@Test
	void should_create_user() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(createAnyUsername(), createAnyUserPassword()));

		// then
		assertThat(thrown).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_null_username() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(null, createAnyUserPassword()));

		// then
		assertThat(thrown).isInstanceOf(NullPointerException.class);
	}

	@Test
	void should_throw_exception_when_null_userId() {
		// given
		final User user = new User(createAnyUsername(), createAnyUserPassword());

		// when
		final Throwable thrown = catchThrowable(user::getUserId);

		// then
		assertThat(thrown).isInstanceOf(NullPointerException.class);
	}

}
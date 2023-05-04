package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.CreateUserSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

class UserTest {

	@Test
	void should_create_user() {
		// when
		final Throwable thrown = catchThrowable(() -> CreateUserSample.builder().build());

		// then
		assertThat(thrown).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_null_username() {
		// when
		final Throwable thrown = catchThrowable(() -> CreateUserSample.builder().username(new Username(null)).build());

		// then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_throw_exception_when_null_userId() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(createAnyUsername(), null, createAnyUserPassword()));

		// then
		assertThat(thrown).isInstanceOf(NullPointerException.class);
	}

}
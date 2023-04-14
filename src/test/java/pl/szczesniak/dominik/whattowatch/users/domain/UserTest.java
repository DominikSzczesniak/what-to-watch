package pl.szczesniak.dominik.whattowatch.users.domain;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UserTest {

	@Test
	void should_create_user() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(new Username("Patryk"), new UserId(5), new UserPassword("asd")));

		// then
		assertThat(thrown).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_null_username() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(null, new UserId(1), new UserPassword("asd")));

		// then
		assertThat(thrown).isInstanceOf(NullPointerException.class);
	}

	@Test
	void should_throw_exception_when_null_userId() {
		// when
		final Throwable thrown = catchThrowable(() -> new User(new Username("dominik"), null, new UserPassword("asd")));

		// then
		assertThat(thrown).isInstanceOf(NullPointerException.class);
	}

}
package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUserIdValueException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UserIdTest {

	@Test
	void should_create_userid_() {
		// when
		final Throwable thrown = catchThrowable(() -> new UserId(1));

		// then
		assertThat(thrown).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_null_username() {
		// when
		final Throwable thrownOne = catchThrowable(() -> new UserId(0));
		final Throwable thrownTwo = catchThrowable(() -> new UserId(-2));

		// then
		assertThat(thrownOne).isInstanceOf(InvalidUserIdValueException.class);
		assertThat(thrownTwo).isInstanceOf(InvalidUserIdValueException.class);
	}

}
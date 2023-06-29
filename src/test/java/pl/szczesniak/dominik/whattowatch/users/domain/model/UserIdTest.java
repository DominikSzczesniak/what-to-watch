package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

class UserIdTest {

	@Test
	void should_create_userid() {
		// when
		final Throwable thrown = catchThrowable(() -> new UserId(1L));

		// then
		assertThat(thrown).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_illegal_userid() {
		assertThatThrownBy(() -> new UserId(0L)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new UserId(-1L)).isInstanceOf(IllegalArgumentException.class);
	}

}
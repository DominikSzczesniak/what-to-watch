package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UsernameTest {

	@Test
	void should_create_valid_username() {
		// when
		final Throwable thrown = catchThrowable(() -> new Username("Dominik"));

		// then
		assertThat(thrown).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_invalid_username() {
		// when
		final Throwable thrown1 = catchThrowable(() -> new Username("D123"));
		final Throwable thrown2 = catchThrowable(() -> new Username("123"));
		final Throwable thrown3 = catchThrowable(() -> new Username("Dominik-"));
		final Throwable thrown4 = catchThrowable(() -> new Username(" Patryk 1 2 3"));
		final Throwable thrown5 = catchThrowable(() -> new Username(""));
		final Throwable thrown6 = catchThrowable(() -> new Username("aaaaaaqyesadasaaaaaaqyesadasaaaaaaqyesadasaaaaaaqyesadasaaaaaaqyesadas"));

		// then
		assertThat(thrown1).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown2).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown3).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown4).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown5).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown6).isInstanceOf(IllegalArgumentException.class);
	}

}
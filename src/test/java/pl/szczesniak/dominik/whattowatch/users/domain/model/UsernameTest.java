package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.Username.usernameContainsOnlyLetters;

class UsernameTest {

	@Test
	void should_return_tre_when_valid_string() {
		// when
		final boolean check = usernameContainsOnlyLetters("Dominik");

		// then
		assertThat(check).isTrue();
	}

	@Test
	void should_return_false_when_empty_string() {
		// when
		final boolean check = usernameContainsOnlyLetters("");

		// then
		assertThat(check).isFalse();

	}

	@Test
	void should_return_false_when_invalid_string() {
		// when
		final boolean check1 = usernameContainsOnlyLetters("123");
		final boolean check2 = usernameContainsOnlyLetters("asd1");
		final boolean check3 = usernameContainsOnlyLetters("Dominik-");
		final boolean check4 = usernameContainsOnlyLetters(" Patryk 1 2 3");

		// then
		assertThat(check1).isFalse();
		assertThat(check2).isFalse();
		assertThat(check3).isFalse();
		assertThat(check4).isFalse();

	}

	@Test
	void should_create_valid_username() {
		// when
		final Username username = new Username("Dominik");

		// then
		assertThat(username.getValue()).isNotInstanceOf(InvalidUsernameException.class);
	}

	@Test
	void should_throw_exception_when_invalid_username() {
		// when
		final Throwable thrown = catchThrowable(() -> new Username("D123"));

		// then
		assertThat(thrown).isInstanceOf(InvalidUsernameException.class);
	}

}
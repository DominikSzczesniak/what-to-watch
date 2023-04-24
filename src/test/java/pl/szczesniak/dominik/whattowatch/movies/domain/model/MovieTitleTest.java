package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


class MovieTitleTest {

	@Test
	void should_create_movie_title() {
		// when
		final Throwable thrown1 = catchThrowable(() -> new MovieTitle("Parasite"));
		final Throwable thrown2 = catchThrowable(() -> new MovieTitle("Star Wars"));
		final Throwable thrown3 = catchThrowable(() -> new MovieTitle("Star Wars 2"));

		// then
		assertThat(thrown1).doesNotThrowAnyException();
		assertThat(thrown2).doesNotThrowAnyException();
		assertThat(thrown3).doesNotThrowAnyException();
	}

	@Test
	void should_throw_exception_when_given_null_as_title() {
		// when
		Throwable thrown = catchThrowable(() -> new MovieTitle(null));

		// then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_throw_exception_when_given_invalid_title() {
		// when
		Throwable thrown1 = catchThrowable(() -> new MovieTitle(" "));
		Throwable thrown2 = catchThrowable(() -> new MovieTitle("--"));
		Throwable thrown3 = catchThrowable(() -> new MovieTitle(" Movie"));

		// then
		assertThat(thrown1).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown2).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrown3).isInstanceOf(IllegalArgumentException.class);
	}

}
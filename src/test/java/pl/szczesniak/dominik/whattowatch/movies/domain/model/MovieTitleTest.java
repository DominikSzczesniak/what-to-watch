package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


class MovieTitleTest {

	@Test
	void should_create_movie_title() {
		// when
		final MovieTitle movieTitle1 = new MovieTitle("Parasite");
		final MovieTitle movieTitle2 = new MovieTitle("Star Wars");
		final MovieTitle movieTitle3 = new MovieTitle("Star Wars 2");

		// then
		assertThat(movieTitle1.getValue()).isNotInstanceOf(IllegalArgumentException.class);
		assertThat(movieTitle2.getValue()).isNotInstanceOf(IllegalArgumentException.class);
		assertThat(movieTitle2.getValue()).isNotInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_throw_exception_when_given_null_as_title() {
		// when
		Throwable thrown = catchThrowable(() -> new MovieTitle(null));

		// then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_throw_exception_when_given_invalid_as_title() {
		// when
		Throwable thrown = catchThrowable(() -> new MovieTitle(" "));

		// then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

}
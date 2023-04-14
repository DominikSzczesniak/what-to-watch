package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle.movieTitleIsValid;


class MovieTitleTest {

	@Test
	void should_return_true_when_given_valid_title() {
		// when
		boolean check1 = movieTitleIsValid("Star Wars 2");
		boolean check2 = movieTitleIsValid("100");

		// then
		assertThat(check1).isTrue();
		assertThat(check2).isTrue();
	}

	@Test
	void should_return_false_when_empty_string() {
		// when
		final boolean check = movieTitleIsValid("");

		// then
		assertThat(check).isFalse();
	}

	@Test
	void should_return_false_when_invalid_string() {
		// when
		final boolean check1 = movieTitleIsValid(" ");
		final boolean check2 = movieTitleIsValid("--");
		final boolean check3 = movieTitleIsValid(" Movie");

		// then
		assertThat(check1).isFalse();
		assertThat(check2).isFalse();
		assertThat(check3).isFalse();
	}

	@Test
	void should_create_movie_title() {
		// when
		final MovieTitle movieTitle1 = new MovieTitle("Parasite");
		final MovieTitle movieTitle2 = new MovieTitle("Star Wars");
		final MovieTitle movieTitle3 = new MovieTitle("Star Wars 2");

		// then
		assertThat(movieTitle1.getValue()).isNotInstanceOf(IllegalArgumentException.class);
		assertThat(movieTitle2.getValue()).isNotInstanceOf(IllegalArgumentException.class);
		assertThat(movieTitle3.getValue()).isNotInstanceOf(IllegalArgumentException.class);
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
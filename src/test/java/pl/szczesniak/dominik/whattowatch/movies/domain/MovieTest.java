package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;

class MovieTest {

	@Test
	void watchedmovies_fields_should_equal_movies_fields() {
		// given
		final Movie movie = MovieSample.builder().build();
		movie.setId(createAnyMovieId().getValue());

		// when
		final WatchedMovie watchedMovie = movie.markAsWatched();

		// then
		assertThat(watchedMovie).extracting(WatchedMovie::getMovieId, WatchedMovie::getUserId, WatchedMovie::getTitle)
				.containsExactlyInAnyOrder(movie.getMovieId(), movie.getUserId(), movie.getTitle());
	}

}
package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class MovieTest {

	@Test
	void watchedmovies_fields_should_equal_movies_fields() {
		// given
		final Movie movie = new Movie(createAnyUserId(), createAnyMovieTitle());
		setId(movie, createAnyMovieId().getValue());

		// when
		final WatchedMovie watchedMovie = movie.markAsWatched();

		// then
		assertThat(watchedMovie).extracting(WatchedMovie::getMovieId, WatchedMovie::getUserId, WatchedMovie::getTitle)
				.containsExactlyInAnyOrder(movie.getMovieId(), movie.getUserId(), movie.getTitle());
	}

	private void setId(final Movie movie, final int id) {
		final Class<Movie> movieClass = Movie.class;
		final Class<? super Movie> baseEntityClass = movieClass.getSuperclass();
		try {
			final Field movieId = baseEntityClass.getDeclaredField("id");
			movieId.setAccessible(true);
			movieId.set(movie, id);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
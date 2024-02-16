package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

public interface MoviesRepository {

	void create(Movie movie);

	void update(Movie movie);

	void removeMovie(MovieId movieId, UserId userId);

	Optional<Movie> findBy(MovieId movieId, UserId userId);

	default Movie getMovie(final MovieId movieId, final UserId userId) {
		return findBy(movieId, userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
	}

}
package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

interface MoviesToWatchRepository {

	void create(Movie movie);

	void update(Movie movie);

	void removeMovie(MovieId movieId, UserId userId);

	Optional<Movie> findBy(MovieId movieId, UserId userId);

	default Movie getMovie(final MovieId movieId, final UserId userId) {
		return findBy(movieId, userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
	}

	void removeAllBy(UserId userId);

}

@Repository
interface SpringDataJpaMoviesRepository extends MoviesToWatchRepository, JpaRepository<Movie, Integer> {

	@Override
	default void removeAllBy(UserId userId) {
		deleteAllByUserId(userId);
	}

	@Override
	default void removeMovie(MovieId movieId, UserId userId) {
		deleteByIdAndUserId(movieId.getValue(), userId);
	}

	@Override
	default void create(Movie movie) {
		save(movie);
	}

	@Override
	default void update(Movie movie) {
		save(movie);
	}

	@Override
	default Optional<Movie> findBy(MovieId movieId, UserId userId) {
		return findByIdAndUserId(movieId.getValue(), userId);
	}

	Optional<Movie> findByIdAndUserId(Integer movieId, UserId userId);

	void deleteByIdAndUserId(final Integer id, final UserId userId);

	void deleteAllByUserId(final UserId userId);

}
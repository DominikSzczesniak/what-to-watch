package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

interface WatchedMoviesRepository {

	void add(WatchedMovie watchedMovie);

	void removeAllBy(UserId userId);

}


@Repository
interface SpringDataJpaWatchedMoviesRepository extends WatchedMoviesRepository, JpaRepository<WatchedMovie, MovieId> {

	@Override
	default void removeAllBy(UserId userId) {
		deleteAllByUserId(userId);
	}

	@Override
	default void add(WatchedMovie watchedMovie) {
		save(watchedMovie);
	}

	void deleteAllByUserId(final UserId userId);

}
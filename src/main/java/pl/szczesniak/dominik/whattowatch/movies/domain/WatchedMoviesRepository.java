package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;

interface WatchedMoviesRepository {

	void add(WatchedMovie watchedMovie);

}


@Repository
interface SpringDataJpaWatchedMoviesRepository extends WatchedMoviesRepository, JpaRepository<WatchedMovie, MovieId> {

	@Override
	default void add(WatchedMovie watchedMovie) {
		save(watchedMovie);
	}

}
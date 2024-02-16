package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaWatchedMoviesRepository implements WatchedMoviesRepository {

	private final SpringDataJpaWatchedMoviesRepository springDataJpaWatchedMoviesRepository;

	@Override
	public void add(final WatchedMovie watchedMovie) {
		springDataJpaWatchedMoviesRepository.save(watchedMovie);
	}

}

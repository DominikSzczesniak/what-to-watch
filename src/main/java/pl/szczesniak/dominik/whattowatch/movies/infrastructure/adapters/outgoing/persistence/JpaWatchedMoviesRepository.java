package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.WatchedMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.WatchedMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaWatchedMoviesRepository implements WatchedMoviesRepository {

	private final SpringDataJpaWatchedMoviesRepository springDataJpaWatchedMoviesRepository;

	@Override
	public void add(final WatchedMovie watchedMovie) {
		springDataJpaWatchedMoviesRepository.save(watchedMovie);
	}

	@Override
	public List<WatchedMovie> findAllBy(final UserId userId) {
		return springDataJpaWatchedMoviesRepository.findAllByUserId(userId);
	}

}

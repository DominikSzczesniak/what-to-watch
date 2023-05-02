package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.WatchedMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.WatchedMoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryWatchedMoviesRepository implements WatchedMoviesRepository {

	private final Map<MovieId, WatchedMovie> watchedMovies = new HashMap<>();

	@Override
	public void add(final WatchedMovie watchedMovie) {
		watchedMovies.put(watchedMovie.getMovieId(), watchedMovie);
	}

	@Override
	public List<WatchedMovie> findAllBy(final UserId userId) {
		return watchedMovies.values().stream()
				.filter(movie -> movie.getUserId().equals(userId))
				.collect(Collectors.toList());
	}
}

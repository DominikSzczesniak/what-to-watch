package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.query.WatchedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryWatchedMoviesRepository implements WatchedMoviesRepository, WatchedMoviesQueryService {

	private final Map<MovieId, WatchedMovie> watchedMovies = new HashMap<>();

	@Override
	public void add(final WatchedMovie watchedMovie) {
		watchedMovies.put(watchedMovie.getMovieId(), watchedMovie);
	}

	@Override
	public List<WatchedMovieQueryResult> getWatchedMovies(final UserId userId) {
		final List<WatchedMovie> foundMovies = watchedMovies.values().stream()
				.filter(movie -> movie.getUserId().equals(userId))
				.toList();

		return mapToQueryResult(foundMovies);
	}

	private static List<WatchedMovieQueryResult> mapToQueryResult(final List<WatchedMovie> foundMovies) {
		return foundMovies.stream().map(watchedMovie -> new WatchedMovieQueryResult(
				watchedMovie.getMovieId().getValue(), watchedMovie.getTitle().getValue(), watchedMovie.getUserId().getValue())).toList();
	}

}

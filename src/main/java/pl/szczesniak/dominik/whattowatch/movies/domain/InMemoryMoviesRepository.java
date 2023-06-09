package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMoviesRepository implements MoviesRepository {

	private final AtomicInteger nextId = new AtomicInteger(0);
	private final Map<MovieId, Movie> movies = new HashMap<>();

	@Override
	public void create(final Movie movie) {
		movie.setMovieId(nextId.incrementAndGet());
		movies.put(movie.getMovieId(), movie);
	}

	@Override
	public void update(final Movie movie) {
		movies.replace(movie.getMovieId(), movie);
	}

	@Override
	public List<Movie> findAll(final UserId userId) {
		return movies.values().stream()
				.filter(movie -> movie.getUserId().equals(userId))
				.collect(Collectors.toList());
	}

	@Override
	public void removeMovie(final MovieId movieId, final UserId userId) {
		if (movieBelongsToUser(movieId, userId)) {
			movies.remove(movieId);
		} else {
			System.out.println("Didn't remove movie.");
		}
	}

	private boolean movieBelongsToUser(final MovieId movieId, final UserId userId) {
		return movies.values().stream().anyMatch(movie -> movie.getMovieId().equals(movieId) && movie.getUserId().equals(userId));
	}

	@Override
	public Optional<Movie> findBy(final MovieId movieId, final UserId userId) {
		return movies.values().stream()
				.filter(movie -> movie.getMovieId().equals(movieId) && movie.getUserId().equals(userId))
				.findFirst();
	}

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMoviesRepository implements MoviesRepository, TagsQuery {

	private final AtomicInteger nextId = new AtomicInteger(0);
	private final Map<MovieId, Movie> movies = new HashMap<>();
	private final Map<MovieTagId, MovieTag> tags = new HashMap<>();

	@Override
	public void create(final Movie movie) {
		movie.setMovieId(nextId.incrementAndGet());
		movies.put(movie.getMovieId(), movie);
	}

	@Override
	public void update(final Movie movie) {
		movies.replace(movie.getMovieId(), movie);
		movie.getTags().forEach(movieTag -> tags.putIfAbsent(movieTag.getTagId(), movieTag));
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

	@Override
	public List<Movie> findAllMoviesByTagId(final String tagId, final Integer userId) {
		return movies.values().stream()
				.filter(movie -> movie.getTags().stream()
						.anyMatch(movieTag -> movieTag.getTagId().equals(new MovieTagId(UUID.fromString(tagId)))))
				.filter(movie -> movie.getUserId().getValue().equals(userId))
				.collect(Collectors.toList());
	}

	@Override
	public List<MovieTag> findByUserId(final Integer userId) {
		return tags.values().stream()
				.filter(movieTag -> movieTag.getUserId().equals(new UserId(userId)))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<MovieTag> findTagByTagId(final String tagId) {
		return Optional.ofNullable(tags.get(new MovieTagId(UUID.fromString(tagId))));
	}

}

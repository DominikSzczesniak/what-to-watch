package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.lang.reflect.Field;
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
		setId(movie, nextId.incrementAndGet());
		movies.put(movie.getMovieId(), movie);
	}

	private void setId(final Movie movie, final int id) {
		final Class<Movie> movieClass = Movie.class;
		final Class<? super Movie> baseEntityClass = movieClass.getSuperclass();
		try {
			final Field movieId = baseEntityClass.getDeclaredField("id");
			movieId.setAccessible(true);
			movieId.set(movie, id);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
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
	public List<Movie> findAllMoviesByTagIds(final List<MovieTagId> tags, final UserId userId) {
		return movies.values().stream()
				.filter(movie -> tags.stream().allMatch(tag ->
						movie.getTags().stream().anyMatch(movieTag -> tag.equals(movieTag.getTagId()))
				))
				.filter(movie -> movie.getUserId().equals(userId))
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

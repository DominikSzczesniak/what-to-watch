package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.query.MoviesQueryService;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieCommentQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedMovies;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMoviesToWatchRepository implements MoviesToWatchRepository, TagsRepository, MoviesQueryService {

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
	public void remove(final MovieId movieId, final UserId userId) {
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
	public void removeAllBy(final UserId userId) {
		movies.values().removeIf(movie -> movie.getUserId().equals(userId));
	}

	@Override
	public Optional<MovieTag> findTagByTagId(final MovieTagId tagId) {
		return Optional.ofNullable(tags.get(tagId));
	}

	@Override
	public void deleteAllMovieTagsBy(final UserId userId) {
		tags.values().removeIf(movieTag -> movieTag.getUserId().equals(userId));
	}

	@Override
	public PagedMovies getMoviesToWatch(final UserId userId, final Integer page, final Integer moviesPerPage) {
		final List<Movie> foundMovies = movies.values().stream()
				.filter(movie -> movie.getUserId().equals(userId))
				.toList();
		return new PagedMovies(toDto(foundMovies), page, 1, foundMovies.size());
	}

	private static List<MovieInListQueryResult> toDto(final List<Movie> foundMovies) {
		return foundMovies.stream().map(movie -> new MovieInListQueryResult(
								movie.getMovieId().getValue(),
								movie.getTitle().getValue()
						)
				)
				.toList();
	}

	@Override
	public Optional<MovieQueryResult> findMovieQueryResult(final MovieId movieId, final UserId userId) {
		return movies.values().stream()
				.filter(movie -> movie.getMovieId().equals(movieId) && movie.getUserId().equals(userId))
				.findFirst()
				.map(movie -> new MovieQueryResult(
						movie.getMovieId().getValue(),
						movie.getTitle().getValue(),
						mapCommentsToQueryResult(movie.getComments()),
						mapTagsToQueryResult(movie.getTags())
				));
	}

	private static Set<MovieCommentQueryResult> mapCommentsToQueryResult(final Set<MovieComment> comments) {
		return comments.stream().map(comment -> new MovieCommentQueryResult(
				comment.getCommentId().toString(), comment.getText())).collect(Collectors.toSet());
	}


	private static Set<MovieTagQueryResult> mapTagsToQueryResult(final Set<MovieTag> tags) {
		return tags.stream().map(movieTag -> new MovieTagQueryResult(
				movieTag.getTagId().getValue(), movieTag.getLabel().getValue(), movieTag.getUserId().getValue())).collect(Collectors.toSet());
	}

	@Override
	public Optional<MovieTagQueryResult> getTagByTagId(final MovieTagId tagId) {
		return tags.values().stream()
				.filter(tag -> tag.getTagId().equals(tagId))
				.findFirst()
				.map(movieTag -> new MovieTagQueryResult(movieTag.getTagId().getValue(), movieTag.getLabel().getValue(), movieTag.getUserId().getValue()));
	}

	@Override
	public PagedMovies getMoviesByTags(final List<MovieTagId> tags, final UserId userId, final Integer page, final Integer moviesPerPage) {
		final List<Movie> foundMovies = movies.values().stream()
				.filter(movie -> tags.stream().allMatch(tag ->
						movie.getTags().stream().anyMatch(movieTag -> tag.equals(movieTag.getTagId()))
				))
				.filter(movie -> movie.getUserId().equals(userId))
				.collect(Collectors.toList());

		return new PagedMovies(toDto(foundMovies), page, 1, foundMovies.size());
	}

	@Override
	public List<MovieTagQueryResult> getMovieTagsByUserId(final UserId userId) {
		final List<MovieTag> foundTags = tags.values().stream()
				.filter(movieTag -> movieTag.getUserId().equals(userId))
				.toList();
		return foundTags.stream().map(tag -> new MovieTagQueryResult(tag.getTagId().getValue(), tag.getLabel().getValue(), tag.getUserId().getValue())).toList();
	}

}

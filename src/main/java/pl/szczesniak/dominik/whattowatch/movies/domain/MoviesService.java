package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieComment;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesService {

	private final MoviesRepository repository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedRepository;

	public MovieId addMovieToList(final AddMovieToList command) {
		if (!userProvider.exists(command.getUserId())) {
			throw new UserDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
		final Movie movie = new Movie(command.getUserId(), command.getMovieTitle());
		repository.create(movie);
		return movie.getMovieId();
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		repository.removeMovie(movieId, userId);
	}

	public List<Movie> getMoviesToWatch(final UserId userId) {
		checkUserExists(userId);
		return repository.findAll(userId);
	}

	public List<WatchedMovie> getWatchedMovies(final UserId userId) {
		checkUserExists(userId);
		return watchedRepository.findAllBy(userId);
	}

	public void moveMovieToWatchedList(final MoveMovieToWatchedMoviesList command) {
		checkUserExists(command.getUserId());
		final Movie movie = findMovie(command.getMovieId(), command.getUserId());
		final WatchedMovie watchedMovie = movie.markAsWatched();
		watchedRepository.add(watchedMovie);
		repository.removeMovie(command.getMovieId(), command.getUserId());
	}

	private void checkUserExists(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new UserDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

	public void updateMovie(final UpdateMovie command) {
		final Movie movie = findMovie(command.getMovieId(), command.getUserId());
		movie.update(command.getTitle());
		repository.update(movie);
	}

	public Movie findMovie(final MovieId movieId, final UserId userId) {
		Movie movie = repository.findBy(movieId, userId).orElseThrow(() -> new MovieDoesNotExistException("Movie doesn't match userId: " + userId));
		return movie;
	}

	public UUID addCommentToMovie(final UserId userId, final MovieId movieId, final String comment) {
		final Movie movie = findMovie(movieId, userId);
		final UUID commentId = movie.addComment(comment);
		repository.update(movie);
		return commentId;
	}

	public void deleteCommentFromMovie(final UserId userId, final MovieId movieId, final UUID commentId) {
		final Movie movie = findMovie(movieId, userId);
		final Optional<MovieComment> foundComment = movie.getComments().stream().filter(comment -> comment.getCommentId().equals(commentId)).findFirst();
		if (foundComment.isPresent()) {
			movie.deleteComment(foundComment.get());
			repository.update(movie);
		}
	}

}



package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesService {

	private final MoviesRepository repository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedRepository;

	public MovieId addMovieToList(final AddMovieToList command) {
		if (!userProvider.exists(command.getUserId())) {
			throw new UserDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
		final Movie movie = new Movie(repository.nextMovieId(), command.getUserId(), command.getMovieTitle());
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

	private Movie getMovie(final MovieId movieId, final UserId userId) {
		return findMovie(movieId, userId);
	}

	public void updateMovie(final UpdateMovie command) {
		final Movie movie = findMovie(command.getMovieId(), command.getUserId());
		movie.update(command.getTitle());
		repository.update(movie);
	}

	private Movie findMovie(final MovieId movieId, final UserId userId) {
		return repository.findBy(movieId, userId).orElseThrow(() -> new MovieDoesNotExistException("Movie doesn't match userId: " + userId));
	}

}



package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesToWatchService {

	private final MoviesRepository repository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedRepository;

	public MovieId addMovieToList(final String movieTitle, final UserId userId) {
		userCheck(userId);
		final Movie movie = new Movie(repository.nextMovieId(), new MovieTitle(movieTitle), userId);
		repository.save(movie);
		return movie.getMovieId();
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		repository.removeMovie(movieId, userId);
	}

	public List<Movie> getList(final UserId userId) {
		return repository.findAll(userId);
	}

	public void moveMovieToWatchedList(MovieId movieId, UserId userId) {
		userCheck(userId);
		if (repository.findBy(movieId, userId).isEmpty()) {
			throw new MovieDoesNotExistException("No movie matched movieId: " + movieId + " and userId: " + userId + ". Action aborted.");
		}
		final WatchedMovie watchedMovie = new WatchedMovie(movieId, repository.getMovie(movieId, userId).getTitle(), userId);
		watchedRepository.addMovieToWatchedList(watchedMovie);
		repository.removeMovie(movieId, userId);
	}

	private void userCheck(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new UserDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

	public List<WatchedMovie> getWatchedList(UserId userId) {
		return watchedRepository.getWatchedList(userId);
	}

}



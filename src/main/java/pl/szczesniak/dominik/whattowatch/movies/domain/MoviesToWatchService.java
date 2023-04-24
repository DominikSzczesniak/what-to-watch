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

	public MovieId addMovieToList(final MovieTitle movieTitle, final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new UserDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
		final Movie movie = new Movie(repository.nextMovieId(), movieTitle, userId);
		repository.save(movie);
		return movie.getMovieId();
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		repository.removeMovie(movieId, userId);
	}

	public List<Movie> getMoviesToWatch(final UserId userId) {
		return repository.findAll(userId);
	}

	public List<WatchedMovie> getWatchedMovies(final UserId userId) {
		return watchedRepository.findAllBy(userId);
	}

	public void moveMovieToWatchedList(final MovieId movieId, final UserId userId) {
		userCheck(userId);
		final WatchedMovie watchedMovie = new WatchedMovie(
				movieId,
				repository.findBy(movieId, userId).orElseThrow(() -> new MovieDoesNotExistException("Movie doesn't match userId: " + userId)).getTitle(),
				userId
		);
		watchedRepository.add(watchedMovie);
		repository.removeMovie(movieId, userId);
	}

	private void userCheck(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new UserDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

}



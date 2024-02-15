package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoviesWatchlistService {

	private final MoviesRepository repository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedRepository;

	public MovieId addMovieToList(final AddMovieToList command) {
		if (!userProvider.exists(command.getUserId())) {
			throw new ObjectDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
		final Movie movie = new Movie(command.getUserId(), command.getMovieTitle());
		repository.create(movie);
		return movie.getMovieId();
	}

	void removeMovieFromList(final MovieId movieId, final UserId userId) {
		repository.removeMovie(movieId, userId);
	}

//	List<MovieInListQueryResult> getMoviesToWatch(final UserId userId) {
//		checkUserExists(userId);
//		return repository.findAll(userId);
//	}

//	List<WatchedMovieQueryResult> getWatchedMovies(final UserId userId) {
//		checkUserExists(userId);
//		return watchedRepository.findAllBy(userId);
//	}

	void moveMovieToWatchedList(final MoveMovieToWatchedMoviesList command) {
		checkUserExists(command.getUserId());
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		final WatchedMovie watchedMovie = movie.markAsWatched();
		watchedRepository.add(watchedMovie);
		repository.removeMovie(command.getMovieId(), command.getUserId());
	}

	void updateMovie(final UpdateMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.updateMovieTitle(command.getTitle());
		repository.update(movie);
	}

	private Movie getMovie(final MovieId movieId, final UserId userId) {
		return repository.findBy(movieId, userId).orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
	}

//	MovieQueryResult getMovieQueryResult(final MovieId movieId, final UserId userId) {
//		return repository.findBy(movieId, userId).orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
//	}

	private void checkUserExists(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new ObjectDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

}

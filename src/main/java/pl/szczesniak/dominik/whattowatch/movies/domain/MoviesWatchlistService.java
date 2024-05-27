package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
class MoviesWatchlistService {

	private final MoviesToWatchRepository moviesToWatchRepository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedMoviesRepository;

	MovieId addMovieToList(final AddMovieToList command) {
		checkUserExists(command.getUserId());
		final Movie movie = new Movie(command.getUserId(), command.getMovieTitle());
		moviesToWatchRepository.create(movie);
		return movie.getMovieId();
	}

	void checkUserExists(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new ObjectDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
	}

	void removeMovieFromList(final MovieId movieId, final UserId userId) {
		moviesToWatchRepository.remove(movieId, userId);
	}

	void moveMovieToWatchedList(final MoveMovieToWatchedMoviesList command) {
		checkUserExists(command.getUserId());
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		final WatchedMovie watchedMovie = movie.markAsWatched();
		watchedMoviesRepository.add(watchedMovie);
		moviesToWatchRepository.remove(command.getMovieId(), command.getUserId());
	}

	void updateMovie(final UpdateMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.updateMovieTitle(command.getTitle());
		moviesToWatchRepository.update(movie);
	}

	private Movie getMovie(final MovieId movieId, final UserId userId) {
		return moviesToWatchRepository.findBy(movieId, userId).orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
	}

}

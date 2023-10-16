package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesFacade {

	private final MoviesListService moviesListService;

	private final MoviesCoverService movieListService;

	private final MoviesCommentsService moviesCommentsService;

	public MovieId addMovieToList(final AddMovieToList command) {
		return moviesListService.addMovieToList(command);
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		moviesListService.removeMovieFromList(movieId, userId);
	}

	public List<Movie> getMoviesToWatch(final UserId userId) {
		return moviesListService.getMoviesToWatch(userId);
	}

	public List<WatchedMovie> getWatchedMovies(final UserId userId) {
		return moviesListService.getWatchedMovies(userId);
	}

	public void moveMovieToWatchedList(final MoveMovieToWatchedMoviesList command) {
		moviesListService.moveMovieToWatchedList(command);
	}

	public void updateMovie(final UpdateMovie command) {
		moviesListService.updateMovie(command);
	}

	public Movie getMovie(final MovieId movieId, final UserId userId) {
		return moviesListService.getMovie(movieId, userId);
	}

	public MovieCoverDTO getCoverForMovie(final MovieId movieId, final UserId user) {
		return movieListService.getCoverForMovie(movieId, user);
	}

	public void setMovieCover(final SetMovieCover command) {
		movieListService.setMovieCover(command);
	}

	public void deleteCover(final MovieId movieId, final UserId userId) {
		movieListService.deleteCover(movieId, userId);
	}

	public UUID addCommentToMovie(final AddCommentToMovie command) {
		return moviesCommentsService.addCommentToMovie(command);
	}

	public void deleteCommentFromMovie(final DeleteCommentFromMovie command) {
		moviesCommentsService.deleteCommentFromMovie(command);
	}

}

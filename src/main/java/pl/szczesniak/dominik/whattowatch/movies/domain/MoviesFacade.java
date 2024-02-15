package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesFacade {

	private final MoviesWatchlistService moviesWatchlistService;

	private final MoviesCoverService movieListService;

	private final MoviesCommentsService moviesCommentsService;

	private final MoviesTagsService moviesTagsService;

	public MovieId addMovieToList(final AddMovieToList command) {
		return moviesWatchlistService.addMovieToList(command);
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		moviesWatchlistService.removeMovieFromList(movieId, userId);
	}

	public List<MovieInListQueryResult> getMoviesToWatch(final UserId userId) {
		return moviesWatchlistService.getMoviesToWatch(userId);
	}

	public List<WatchedMovieQueryResult> getWatchedMovies(final UserId userId) {
		return moviesWatchlistService.getWatchedMovies(userId);
	}

	public void moveMovieToWatchedList(final MoveMovieToWatchedMoviesList command) {
		moviesWatchlistService.moveMovieToWatchedList(command);
	}

	public void updateMovie(final UpdateMovie command) {
		moviesWatchlistService.updateMovie(command);
	}

	public MovieQueryResult getMovie(final MovieId movieId, final UserId userId) {
		return moviesWatchlistService.getMovieQueryResult(movieId, userId);
	}

	public MovieCoverDTO getCoverForMovie(final MovieId movieId, final UserId user) { // zastanowic sie
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

	public MovieTagId addTagToMovie(final AddTagToMovie command) {
		return moviesTagsService.addTagToMovie(command);
	}

	public Optional<MovieTagQueryResult> getTagByTagId(final MovieTagId tagId) {
		return moviesTagsService.getTagByTagId(tagId);
	}

	public void deleteTagFromMovie(final DeleteTagFromMovie command) {
		moviesTagsService.deleteTagFromMovie(command);
	}

	public List<MovieInListQueryResult> getMoviesByTags(final List<MovieTagId> tags, final UserId userId) {
		return moviesTagsService.getMoviesByTags(tags, userId);
	}

	public List<MovieTagQueryResult> getMovieTagsByUserId(final Integer userId) {
		return moviesTagsService.getMovieTagsByUserId(userId);
	}

}

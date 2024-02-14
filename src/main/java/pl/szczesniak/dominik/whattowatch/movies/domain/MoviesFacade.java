package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
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

	private final MoviesListService moviesListService;

	private final MoviesCoverService movieListService;

	private final MoviesCommentsService moviesCommentsService;

	private final MoviesTagsService moviesTagsService;

	public MovieId addMovieToList(final AddMovieToList command) {
		return moviesListService.addMovieToList(command);
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		moviesListService.removeMovieFromList(movieId, userId);
	}

	public List<MovieQueryResult> getMoviesToWatch(final UserId userId) {
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

	public MovieQueryResult getMovie(final MovieId movieId, final UserId userId) {
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

	public MovieTagId addTagToMovie(final AddTagToMovie command) {
		return moviesTagsService.addTagToMovie(command);
	}

	public Optional<MovieTag> getTagByTagId(final MovieTagId tagId) {
		return moviesTagsService.getTagByTagId(tagId);
	}

	public void deleteTagFromMovie(final DeleteTagFromMovie command) {
		moviesTagsService.deleteTagFromMovie(command);
	}

	public List<Movie> getMoviesByTags(final List<MovieTagId> tags, final UserId userId) {
		return moviesTagsService.getMoviesByTags(tags, userId);
	}

	public List<MovieTag> getMovieTagsByUserId(final Integer userId) {
		return moviesTagsService.getMovieTagsByUserId(userId);
	}

}

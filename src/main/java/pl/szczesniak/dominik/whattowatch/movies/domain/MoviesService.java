package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesService {

	private final MoviesRepository moviesRepository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedRepository;
	private final FilesStorage filesStorage;
	private final TagsQuery tagsQuery;

	public MovieId addMovieToList(final AddMovieToList command) {
		if (!userProvider.exists(command.getUserId())) {
			throw new ObjectDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
		final Movie movie = new Movie(command.getUserId(), command.getMovieTitle());
		moviesRepository.create(movie);
		return movie.getMovieId();
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		moviesRepository.removeMovie(movieId, userId);
	}

	public List<Movie> getMoviesToWatch(final UserId userId) {
		checkUserExists(userId);
		return moviesRepository.findAll(userId);
	}

	public List<WatchedMovie> getWatchedMovies(final UserId userId) {
		checkUserExists(userId);
		return watchedRepository.findAllBy(userId);
	}

	public void moveMovieToWatchedList(final MoveMovieToWatchedMoviesList command) {
		checkUserExists(command.getUserId());
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		final WatchedMovie watchedMovie = movie.markAsWatched();
		watchedRepository.add(watchedMovie);
		moviesRepository.removeMovie(command.getMovieId(), command.getUserId());
	}

	public void updateMovie(final UpdateMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.updateMovieTitle(command.getTitle());
		moviesRepository.update(movie);
	}

	public Movie getMovie(final MovieId movieId, final UserId userId) {
		return moviesRepository.findBy(movieId, userId).orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
	}

	private static MovieCover getMovieCover(final Movie movie) {
		return movie.getCover().orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't have a cover."));
	}

	public MovieCoverDTO getCoverForMovie(final MovieId movieId, final UserId user) {
		checkUserExists(user);
		final Movie movie = getMovie(movieId, user);
		final MovieCover movieCover = getMovieCover(movie);
		final InputStream coverContent = filesStorage.findStoredFileContent(movieCover.getCoverId())
				.orElseThrow(() -> new ObjectDoesNotExistException("Cover content is empty"));
		return new MovieCoverDTO(movieCover.getFilename(), movieCover.getCoverContentType(),
				coverContent);
	}

	public void setMovieCover(final SetMovieCover command) {
		checkUserExists(command.getUserId());
		final StoredFileId storedFileId = filesStorage.store(command.getCoverContent());
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.updateCover(
				new MovieCover(command.getCoverFilename(), command.getCoverContentType(), storedFileId.getValue())
		);
		moviesRepository.update(movie);
	}

	public void deleteCover(final MovieId movieId, final UserId userId) {
		checkUserExists(userId);
		final Movie movie = getMovie(movieId, userId);
		final MovieCover movieCover = getMovieCover(movie);
		filesStorage.deleteFile(movieCover.getCoverId());
		movie.updateCover(null);
		moviesRepository.update(movie);
	}

	public UUID addCommentToMovie(final AddCommentToMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		final UUID commentId = movie.addComment(command.getComment());
		moviesRepository.update(movie);
		return commentId;
	}

	public void deleteCommentFromMovie(final DeleteCommentFromMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.deleteComment(command.getCommentId());
		moviesRepository.update(movie);
	}

	private void checkUserExists(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new ObjectDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

	public MovieTagId addTagToMovie(final AddTagToMovie command) {
		final MovieTagId tagId = command.getTagId().orElse(new MovieTagId(UUID.randomUUID()));
		final Optional<MovieTag> movieTag = checkMovieTagBelongsToUser(command.getUserId(), tagId);
		final MovieTagLabel tagLabel = movieTag.map(MovieTag::getLabel).orElse(command.getTagLabel());

		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.addTag(tagId,	tagLabel, command.getUserId());

		moviesRepository.update(movie);
		return tagId;
	}

	private Optional<MovieTag> checkMovieTagBelongsToUser(final UserId userId, final MovieTagId tagId) {
		final Optional<MovieTag> tagByTagId = getTagByTagId(tagId);
		if (tagByTagId.isPresent() && !tagByTagId.get().getUserId().equals(userId)) {
			throw new ObjectDoesNotExistException("MovieTag does not belong to user");
		}
		return tagByTagId;
	}

	public Optional<MovieTag> getTagByTagId(final MovieTagId tagId) {
		return tagsQuery.findTagByTagId(tagId.getValue().toString());
	}

	public void deleteTagFromMovie(final DeleteTagFromMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.deleteTag(command.getTagId());
		moviesRepository.update(movie);
	}

	public List<MovieId> getMoviesByTagId(final MovieTagId tagId, final UserId userId) {
		return tagsQuery.findAllMoviesByTagId(tagId.getValue().toString(), userId.getValue());
	}


	public List<MovieTag> getMovieTagsByUserId(final Integer userId) {
		return tagsQuery.findByUserId(userId);
	}

}
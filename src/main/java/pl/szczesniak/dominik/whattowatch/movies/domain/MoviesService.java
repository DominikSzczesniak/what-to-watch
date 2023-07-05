package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesService {

	private final MoviesRepository repository;
	private final UserProvider userProvider;
	private final WatchedMoviesRepository watchedRepository;
	private final FilesStorage filesStorage;

	public MovieId addMovieToList(final AddMovieToList command) {
		if (!userProvider.exists(command.getUserId())) {
			throw new ObjectDoesNotExistException("User doesn't exist. Didn't add movie to any list");
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
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		final WatchedMovie watchedMovie = movie.markAsWatched();
		watchedRepository.add(watchedMovie);
		repository.removeMovie(command.getMovieId(), command.getUserId());
	}

	public void updateMovie(final UpdateMovie command) {
		final Movie movie = getMovie(command.getMovieId(), command.getUserId());
		movie.update(command.getTitle());
		repository.update(movie);
	}

	private Movie getMovie(final MovieId movieId, final UserId userId) {
		return repository.findBy(movieId, userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't match userId: " + userId));
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
				new MovieCover(command.getCoverFilename(), command.getCoverContentType())
		);
		movie.getCover().get().setCoverId(storedFileId);
		repository.update(movie);
	}

	public void deleteCover(final MovieId movieId, final UserId userId) {
		checkUserExists(userId);
		final Movie movie = getMovie(movieId, userId);
		final MovieCover movieCover = getMovieCover(movie);
		filesStorage.deleteFile(movieCover.getCoverId());
		movie.updateCover(null);
		repository.update(movie);
	}

	private static MovieCover getMovieCover(final Movie movie) {
		return movie.getCover().orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't have a cover."));
	}

	private void checkUserExists(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new ObjectDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

}
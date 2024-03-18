package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
class MoviesCoverService {

	private final MoviesToWatchRepository moviesRepository;
	private final UserProvider userProvider;
	private final FilesStorage filesStorage;


	private static MovieCover getMovieCover(final Movie movie) {
		return movie.getCover().orElseThrow(() -> new ObjectDoesNotExistException("Movie doesn't have a cover."));
	}

	MovieCoverDTO getCoverForMovie(final MovieId movieId, final UserId user) {
		checkUserExists(user);
		final Movie movie = moviesRepository.getMovie(movieId, user);
		final MovieCover movieCover = getMovieCover(movie);
		final InputStream coverContent = filesStorage.findStoredFileContent(movieCover.getCoverId())
				.orElseThrow(() -> new ObjectDoesNotExistException("Cover content is empty"));
		return new MovieCoverDTO(movieCover.getFilename(), movieCover.getCoverContentType(),
				coverContent);
	}

	void setMovieCover(final SetMovieCover command) {
		checkUserExists(command.getUserId());
		final StoredFileId storedFileId = filesStorage.store(command.getCoverContent());
		final Movie movie = moviesRepository.getMovie(command.getMovieId(), command.getUserId());
		movie.updateCover(
				new MovieCover(command.getCoverFilename(), command.getCoverContentType(), storedFileId.getValue())
		);
		moviesRepository.update(movie);
	}

	void deleteCover(final MovieId movieId, final UserId userId) {
		checkUserExists(userId);
		final Movie movie = moviesRepository.getMovie(movieId, userId);
		final MovieCover movieCover = getMovieCover(movie);
		filesStorage.deleteFile(movieCover.getCoverId());
		movie.updateCover(null);
		moviesRepository.update(movie);
	}

	private void checkUserExists(final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new ObjectDoesNotExistException("User with userId: " + userId + " doesn't exist. Action aborted");
		}
	}

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserDeletedService {

	private final MoviesToWatchRepository moviesToWatchRepository;
	private final WatchedMoviesRepository watchedRepository;
	private final TagsRepository tagsService;
	private final FilesStorage filesStorage;

	void removeAllDeletedUsersData(final UserId userId) { // todo: sprawdzic czy wystarczy usuwanie tylko filmow
		moviesToWatchRepository.removeAllBy(userId);
		watchedRepository.removeAllBy(userId);
		tagsService.deleteAllMovieTagsBy(userId); // to potrzebne
//		final List<Movie> allMoviesWithCovers = moviesToWatchRepository.findAllMoviesWithCovers(userId);
//		deleteCovers(allMoviesWithCovers);
	}

	private void deleteCovers(final List<Movie> moviesWithCovers) {
		moviesWithCovers.forEach(movie -> {
			filesStorage.deleteFile(movie.getCover().get().getCoverId());
			movie.updateCover(null);
			moviesToWatchRepository.update(movie);
		});
	}

}

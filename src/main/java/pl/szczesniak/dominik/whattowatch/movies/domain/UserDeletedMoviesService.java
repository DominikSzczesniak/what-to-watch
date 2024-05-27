package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
class UserDeletedMoviesService {

	private final MoviesToWatchRepository moviesToWatchRepository;
	private final WatchedMoviesRepository watchedRepository;
	private final TagsRepository tagsRepository;

	public void removeAllDeletedUsersData(final UserId userId) {
		watchedRepository.removeAllBy(userId);
		moviesToWatchRepository.removeAllBy(userId);
		tagsRepository.deleteAllMovieTagsBy(userId);
	}

}

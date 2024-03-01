package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence.InMemoryFilesStorage;

public class TestMoviesFacadeConfiguration {

	static MoviesFacade moviesFacade(final UserProvider userProvider) {
		final InMemoryMoviesToWatchRepository repository = new InMemoryMoviesToWatchRepository();
		final InMemoryWatchedMoviesRepository watchedRepository = new InMemoryWatchedMoviesRepository();
		return new MoviesFacadeConfiguration()
				.moviesFacade(
						new MoviesWatchlistService(repository, userProvider, watchedRepository),
						new MoviesCoverService(repository, userProvider, new InMemoryFilesStorage()),
						new MoviesCommentsService(repository),
						new MoviesTagsService(repository, repository),
						repository,
						watchedRepository
				);
	}

}

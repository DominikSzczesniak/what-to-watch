package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence.InMemoryFilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence.InMemoryWatchedMoviesRepository;

public class TestMoviesToWatchServiceConfiguration {

	static MoviesService moviesToWatchService(final UserProvider userProvider) {
		final InMemoryMoviesRepository inMemoryMoviesRepository = new InMemoryMoviesRepository();
		return new MoviesServiceConfiguration()
				.moviesService(
						inMemoryMoviesRepository,
						userProvider,
						new InMemoryWatchedMoviesRepository(),
						new InMemoryFilesStorage(),
						inMemoryMoviesRepository
				);
	}

}

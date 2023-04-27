package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryWatchedMoviesRepository;

public class TestMoviesToWatchServiceConfiguration {

	static MoviesService moviesToWatchService(final UserProvider userProvider) {
		return new MoviesToWatchServiceConfiguration()
				.moviesToWatchService(new InMemoryMoviesRepository(), userProvider, new InMemoryWatchedMoviesRepository());
	}

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryWatchedMoviesRepository;

public class TestMoviesToWatchServiceConfiguration {

	static MoviesService moviesToWatchService(final UserProvider userProvider) {
		return new MoviesServiceConfiguration()
				.moviesService(new InMemoryMoviesRepository(), userProvider, new InMemoryWatchedMoviesRepository());
	}

}

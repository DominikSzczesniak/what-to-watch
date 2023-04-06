package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;

public class TestMoviesToWatchServiceConfiguration {

    static MoviesToWatchService moviesToWatchService(final UserProvider userProvider) {
        return new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository(), userProvider);
    }

}

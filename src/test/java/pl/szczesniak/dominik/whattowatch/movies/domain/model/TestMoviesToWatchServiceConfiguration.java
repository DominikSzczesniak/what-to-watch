package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;

public class TestMoviesToWatchServiceConfiguration {

    static MoviesToWatchService moviesToWatchService() {
        return new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository());
    }

}

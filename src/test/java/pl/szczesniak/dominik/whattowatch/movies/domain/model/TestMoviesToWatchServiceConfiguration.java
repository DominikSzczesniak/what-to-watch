package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

public class TestMoviesToWatchServiceConfiguration {


    static MoviesToWatchService moviesToWatchService(UserService service) {
        return new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository(), service);
    }

}

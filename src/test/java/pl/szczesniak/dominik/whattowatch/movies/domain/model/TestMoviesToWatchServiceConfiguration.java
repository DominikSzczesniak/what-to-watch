package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUsersRepository;

public class TestMoviesToWatchServiceConfiguration {

    static MoviesToWatchService moviesToWatchService() {
        return new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository());
    }

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.UserProvider;

public class TestMoviesToWatchServiceConfiguration {


    static MoviesToWatchService moviesToWatchService(MoviesRepository moviesRepository, UserProvider userProvider) {
        return new MoviesToWatchServiceConfiguration().moviesToWatchService(moviesRepository, userProvider);
    }

}

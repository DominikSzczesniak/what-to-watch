package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

public class MoviesToWatchServiceConfiguration {

    public  MoviesToWatchService moviesToWatchService(final MoviesRepository moviesRepository) {
        return new MoviesToWatchService(moviesRepository);
    }

}

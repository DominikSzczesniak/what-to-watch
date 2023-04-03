package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

public class MoviesToWatchServiceConfiguration {

    public MoviesToWatchService moviesToWatchService(final MoviesRepository moviesRepository, final UserService userService) {
        return new MoviesToWatchService(moviesRepository, userService);
    }

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.UserService;

public class MoviesToWatchServiceConfiguration {

    public MoviesToWatchService moviesToWatchService(final MoviesRepository moviesRepository, final UserProvider userProvider) {
        return new MoviesToWatchService(moviesRepository, userProvider);
    }

    public UserProvider userProvider (final UserService userService) {
        return userService::exists;
    }

}

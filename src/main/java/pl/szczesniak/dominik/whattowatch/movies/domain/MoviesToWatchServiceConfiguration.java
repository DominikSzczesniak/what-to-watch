package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.UserService;

public class MoviesToWatchServiceConfiguration {

	public MoviesService moviesToWatchService(final MoviesRepository moviesRepository, final UserProvider userProvider, final WatchedMoviesRepository watchedMoviesRepository) {
		return new MoviesService(moviesRepository, userProvider, watchedMoviesRepository);
	}

	public UserProvider userProvider(final UserService userService) {
		return userService::exists;
	}

}

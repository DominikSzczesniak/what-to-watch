package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;

@Configuration
public class MoviesServiceConfiguration {

	@Bean
	public MoviesService moviesService(final MoviesRepository moviesRepository,
									   final UserProvider userProvider,
									   final WatchedMoviesRepository watchedMoviesRepository,
									   final FilesStorage filesStorage,
									   final TagsQuery tagsQuery) {
		return new MoviesService(moviesRepository, userProvider, watchedMoviesRepository, filesStorage, tagsQuery);
	}

	@Bean
	public UserProvider userProvider(final UserService userService) {
		return userService::exists;
	}

}

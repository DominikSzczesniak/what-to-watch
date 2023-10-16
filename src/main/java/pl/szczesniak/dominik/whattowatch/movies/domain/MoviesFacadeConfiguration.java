package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;

@Configuration
public class MoviesFacadeConfiguration {

	@Bean
	public MoviesFacade moviesService(final MoviesListService moviesListService,
									  final MoviesCoverService movieListService,
									  final MoviesCommentsService moviesCommentsService) {
		return new MoviesFacade(moviesListService, movieListService, moviesCommentsService);
	}

	@Bean
	public UserProvider userProvider(final UserService userService) {
		return userService::exists;
	}

}
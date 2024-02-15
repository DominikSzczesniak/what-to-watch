package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;

@Configuration
public class MoviesFacadeConfiguration {

	@Bean
	public MoviesFacade moviesService(final MoviesWatchlistService moviesWatchlistService,
									  final MoviesCoverService movieListService,
									  final MoviesCommentsService moviesCommentsService,
									  final MoviesTagsService moviesTagsService) {
		return new MoviesFacade(moviesWatchlistService, movieListService, moviesCommentsService, moviesTagsService);
	}

	@Bean
	public UserProvider userProvider(final UserService userService) {
		return userService::exists;
	}

}
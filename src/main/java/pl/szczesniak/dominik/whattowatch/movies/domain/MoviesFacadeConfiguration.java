package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.query.MoviesQueryService;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.query.WatchedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.users.domain.UserFacade;

@Configuration
public class MoviesFacadeConfiguration {

	@Bean
	public MoviesFacade moviesFacade(final MoviesWatchlistService moviesWatchlistService,
									 final MoviesCoverService movieListService,
									 final MoviesCommentsService moviesCommentsService,
									 final MoviesTagsService moviesTagsService,
									 final MoviesQueryService moviesQueryService,
									 final WatchedMoviesQueryService watchedMoviesQueryService) {
		return new MoviesFacade(
				moviesWatchlistService,
				movieListService,
				moviesCommentsService,
				moviesTagsService,
				moviesQueryService,
				watchedMoviesQueryService
		);
	}

	@Bean
	public UserProvider userProvider(final UserFacade userFacade) {
		return userFacade::exists;
	}

}
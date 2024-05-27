package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.query.MoviesQueryService;
import pl.szczesniak.dominik.whattowatch.movies.query.WatchedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.users.domain.UserFacade;

@Configuration
class MoviesFacadeConfiguration {

	@Bean
	public MoviesFacade moviesFacade(final MoviesToWatchRepository moviesToWatchRepository,
									 final UserProvider userProvider,
									 final WatchedMoviesRepository watchedMoviesRepository,
									 final FilesStorage filesStorage,
									 final TagsRepository tagsRepository,
									 final MoviesQueryService moviesQueryService,
									 final WatchedMoviesQueryService watchedMoviesQueryService) {
		return new MoviesFacade(
				new MoviesWatchlistService(moviesToWatchRepository, userProvider, watchedMoviesRepository),
				new MoviesCoverService(moviesToWatchRepository, userProvider, filesStorage),
				new MoviesCommentsService(moviesToWatchRepository),
				new MoviesTagsService(moviesToWatchRepository, tagsRepository),
				moviesQueryService,
				watchedMoviesQueryService,
				new UserDeletedMoviesService(moviesToWatchRepository, watchedMoviesRepository, tagsRepository)
		);
	}

	@Bean
	public UserProvider userProvider(final UserFacade userFacade) {
		return userFacade::exists;
	}

}
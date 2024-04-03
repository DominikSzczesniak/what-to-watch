package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.transaction.support.TransactionTemplate;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.Clock;

class RecommendationService {

	private final RecommendationConfigurationManager configurationManager;
	private final MovieInfoApi movieInfoApi;
	private final RecommendedMoviesRepository recommendedMoviesRepository;
	private final RecommendationsRepository recommendationsRepository;
	private final Clock clock;
	private final TransactionTemplate transactionTemplate;

	private final int numberOfMoviesToRecommend;

	RecommendationService(final RecommendationConfigurationManager configurationManager,
						  final MovieInfoApi movieInfoApi,
						  final RecommendedMoviesRepository recommendedMoviesRepository,
						  final RecommendationsRepository recommendationsRepository,
						  final Clock clock,
						  final TransactionTemplate transactionTemplate,
						  final int numberOfMoviesToRecommend) {
		this.configurationManager = configurationManager;
		this.movieInfoApi = movieInfoApi;
		this.recommendedMoviesRepository = recommendedMoviesRepository;
		this.recommendationsRepository = recommendationsRepository;
		this.clock = clock;
		this.transactionTemplate = transactionTemplate;
		this.numberOfMoviesToRecommend = numberOfMoviesToRecommend;
	}

	MovieInfoResponse recommendPopularMovies() {
		return movieInfoApi.getPopularMovies();
	}

	public void recommendMoviesByConfiguration(final UserId userId) {
		final RecommendationConfiguration configuration = getRecommendationConfiguration(userId);
		final UserMoviesRecommendations userMoviesRecommendations = recommendationsRepository.findBy(userId)
				.orElseGet(() -> new UserMoviesRecommendations(userId));
		if (!userMoviesRecommendations.hasRecommendedMoviesForCurrentInterval(clock)) {
			final MovieInfoResponse recommendedFromApi = movieInfoApi.getMoviesByGenre(configuration.getGenres());
			RecommendedMovies recommendedMovies = userMoviesRecommendations.recommendMovies(recommendedFromApi.getResults(), configuration.getGenres(), numberOfMoviesToRecommend);

			createInTransaction(userMoviesRecommendations, recommendedMovies);
		}
	}

	private void createInTransaction(final UserMoviesRecommendations userMoviesRecommendations, final RecommendedMovies recommendedMovies) {
		transactionTemplate.executeWithoutResult(status -> {
			recommendedMoviesRepository.create(recommendedMovies); // todo: przez agregat
			recommendationsRepository.create(userMoviesRecommendations);
		});
	}

	private RecommendationConfiguration getRecommendationConfiguration(final UserId userId) {
		return configurationManager.findby(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException(
						"Shouldn't happen, recommendations for user happen only when user has recommendation configuration")
				);
	}

}

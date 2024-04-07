package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.transaction.support.TransactionTemplate;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.RecommendMovies;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

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

	public void recommendMoviesByConfiguration(final RecommendMovies command) {
		final UserMoviesRecommendations userMoviesRecommendations = recommendationsRepository.findBy(command.getUserId())
				.orElseGet(() -> createInTransaction(new UserMoviesRecommendations(command.getUserId())));
		if (!userMoviesRecommendations.hasRecommendedMoviesForCurrentInterval(clock)) {
			final MovieInfoResponse recommendedFromApi = movieInfoApi.getMoviesByGenre(command.getGenres());
			userMoviesRecommendations.recommendMovies(recommendedFromApi.getResults(), command.getGenres(), numberOfMoviesToRecommend);
			updateInTransaction(userMoviesRecommendations);
		}
	}

	private UserMoviesRecommendations createInTransaction(final UserMoviesRecommendations userMoviesRecommendations) {
		transactionTemplate.executeWithoutResult(status -> recommendationsRepository.create(userMoviesRecommendations));
		return userMoviesRecommendations;
	}

	private void updateInTransaction(final UserMoviesRecommendations userMoviesRecommendations) {
		transactionTemplate.executeWithoutResult(status -> recommendationsRepository.update(userMoviesRecommendations));
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;

class RecommendationService {

	private final RecommendationConfigurationManager configurationManager;
	private final MovieInfoApi movieInfoApi;
	private final RecommendedMoviesRepository recommendedMoviesRepository;
	private final RecommendationsRepository recommendationsRepository;
	private final Clock clock;
	private final PlatformTransactionManager transactionManager;

	private final int NUMBER_OF_MOVIES_TO_RECOMMEND;

	RecommendationService(final RecommendationConfigurationManager configurationManager,
						  final MovieInfoApi movieInfoApi,
						  final RecommendedMoviesRepository recommendedMoviesRepository,
						  final RecommendationsRepository recommendationsRepository,
						  final Clock clock,
						  final PlatformTransactionManager transactionManager,
						  final @Value("${number.of.movies.to.recommend}") int numberOfMoviesToRecommend) {
		this.configurationManager = configurationManager;
		this.movieInfoApi = movieInfoApi;
		this.recommendedMoviesRepository = recommendedMoviesRepository;
		this.recommendationsRepository = recommendationsRepository;
		this.clock = clock;
		this.transactionManager = transactionManager;
		this.NUMBER_OF_MOVIES_TO_RECOMMEND = numberOfMoviesToRecommend;
	}

	MovieInfoResponse recommendPopularMovies() {
		return movieInfoApi.getPopularMovies();
	}

	public void recommendMoviesByConfiguration(final UserId userId) {
		if (!hasRecommendedMoviesForCurrentInterval(userId)) {
			final RecommendationConfiguration configuration = getRecommendationConfiguration(userId);
			final UserMoviesRecommendations userMoviesRecommendations = recommendationsRepository.findBy(userId)
					.orElse(new UserMoviesRecommendations(userId));

			final List<MovieInfo> recommendedFromApi = getMovieInfosByConfig(configuration.getGenres());

			generateRecommendedMovies(configuration, userMoviesRecommendations, recommendedFromApi);
		}
	}

	private void generateRecommendedMovies(final RecommendationConfiguration configuration,
										   final UserMoviesRecommendations userMoviesRecommendations,
										   final List<MovieInfo> recommendedFromApi) {
		final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		final TransactionStatus status = transactionManager.getTransaction(def);

		try {
			final RecommendedMovies recommendedMovies = userMoviesRecommendations.recommendMovies(
					recommendedFromApi,
					configuration.getGenres(),
					NUMBER_OF_MOVIES_TO_RECOMMEND
			);

			recommendationsRepository.create(userMoviesRecommendations);
			recommendedMoviesRepository.create(recommendedMovies);
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
		}
	}

	private RecommendationConfiguration getRecommendationConfiguration(final UserId userId) {
		return configurationManager.findby(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException(
						"Shouldn't happen, recommendations for user happen only when user has recommendation configuration")
				);
	}

	boolean hasRecommendedMoviesForCurrentInterval(final UserId userId) {
		final LocalDateTime now = LocalDateTime.now(clock);
		final LocalDateTime intervalStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY)).with(LocalTime.parse("00:00:00"));
		final LocalDateTime intervalEnd = now.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).with(LocalTime.parse("23:59:59"));

		return recommendedMoviesRepository.existsByUserIdAndRecommendationDateBetween(userId, intervalStart, intervalEnd);
	}

	private List<MovieInfo> getMovieInfosByConfig(final Set<MovieGenre> genres) {
		Set<MovieGenre> movieGenres = genres;
		if (genres.size() == 0) {
			movieGenres = MovieGenre.allValues();
		}

		final MovieInfoResponse moviesByGenre = movieInfoApi.getMoviesByGenre(movieGenres);
		return moviesByGenre.getResults();
	}

}

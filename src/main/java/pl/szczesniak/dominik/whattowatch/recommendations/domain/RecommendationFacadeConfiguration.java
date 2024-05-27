package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendationConfigurationQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendedMoviesQueryService;

import java.time.Clock;

@Configuration
class RecommendationFacadeConfiguration {

	@Bean
	RecommendationFacade recommendationFacade(final RecommendationConfigurationRepository configurationRepository,
											  final RecommendationsRepository recommendationsRepository,
											  final MovieInfoApi movieInfoApi,
											  @Value("${number.of.movies.to.recommend}") final int numberOfMoviesToRecommend,
											  final TransactionTemplate transactionTemplate,
											  final RecommendationConfigurationQueryService recommendationConfigurationQueryService,
											  final RecommendedMoviesQueryService recommendedMoviesQueryService) {
		return new RecommendationFacade(
				new RecommendationConfigurationManager(configurationRepository),
				new RecommendationService(movieInfoApi, recommendationsRepository, Clock.systemDefaultZone(), transactionTemplate, numberOfMoviesToRecommend),
				recommendationConfigurationQueryService,
				recommendedMoviesQueryService,
				new UserDeletedRecommendationsService(configurationRepository));
	}

}

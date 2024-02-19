package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query.RecommendationConfigurationQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query.RecommendedMoviesQueryService;

@Configuration
class RecommendationFacadeConfiguration {

	@Bean
	RecommendationFacade recommendationFacade(final RecommendationConfigurationManager manager,
											  final RecommendationService service,
											  final RecommendationConfigurationQueryService recommendationConfigurationQueryService,
											  final RecommendedMoviesQueryService recommendedMoviesQueryService) {
		return new RecommendationFacade(manager, service, recommendationConfigurationQueryService, recommendedMoviesQueryService);
	}

}

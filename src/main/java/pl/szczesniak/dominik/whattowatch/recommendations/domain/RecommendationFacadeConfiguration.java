package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query.RecommendationQueryService;

@Configuration
class RecommendationFacadeConfiguration {

	@Bean
	RecommendationFacade recommendationFacade(final RecommendationConfigurationManager manager,
											  final RecommendationService service,
											  final RecommendationQueryService recommendationQueryService) {
		return new RecommendationFacade(manager, service, recommendationQueryService);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendationFacadeConfiguration {

	@Bean
	RecommendationFacade recommendationFacade(final RecommendationConfigurationManager manager, final RecommendationService service) {
		return new RecommendationFacade(manager, service);
	}

}

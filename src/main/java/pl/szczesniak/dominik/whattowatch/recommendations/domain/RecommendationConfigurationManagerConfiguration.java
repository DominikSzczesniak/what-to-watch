package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendationConfigurationManagerConfiguration {

	@Bean
	RecommendationConfigurationManager recommendationConfigurationManager(final RecommendationConfigurationRepository repository) {
		return new RecommendationConfigurationManager(repository);
	}

}

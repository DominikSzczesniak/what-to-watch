package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendationConfigurationManagerConfiguration {

	@Bean
	public RecommendationConfigurationManager recommendationConfigurationManager(final RecommendationConfigurationRepository repository) {
		return new RecommendationConfigurationManager(repository);
	}

}

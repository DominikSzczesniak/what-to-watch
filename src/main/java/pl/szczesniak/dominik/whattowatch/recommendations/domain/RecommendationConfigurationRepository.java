package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;

public interface RecommendationConfigurationRepository {

	ConfigurationId create(RecommendationConfiguration configuration);

	ConfigurationId update(RecommendationConfiguration configuration);

}

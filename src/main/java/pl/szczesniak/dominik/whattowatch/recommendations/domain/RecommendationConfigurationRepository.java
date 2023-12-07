package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface RecommendationConfigurationRepository {

	ConfigurationId create(RecommendationConfiguration configuration);

	ConfigurationId update(RecommendationConfiguration configuration);

	Optional<RecommendationConfiguration> findBy(UserId userId);

	List<RecommendationConfiguration> findAll();

}

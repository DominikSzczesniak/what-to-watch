package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationRepository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;

@Repository
@RequiredArgsConstructor
public class JpaRecommendationConfigurationRepository implements RecommendationConfigurationRepository {

	private final SpringDataJpaRecommendationConfigurationRepository springDataJpaRecommendationConfigurationRepository;

	@Override
	public ConfigurationId create(final RecommendationConfiguration configuration) {
		return springDataJpaRecommendationConfigurationRepository.save(configuration).getConfigurationId();
	}

	@Override
	public ConfigurationId update(final RecommendationConfiguration configuration) {
		return springDataJpaRecommendationConfigurationRepository.save(configuration).getConfigurationId();
	}
}

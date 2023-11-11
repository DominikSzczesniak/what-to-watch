package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationRepository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaRecommendationConfigurationRepository implements RecommendationConfigurationRepository {

	private final SpringDataJpaRecommendationConfigurationRepository springDataJpaRecommendationConfigurationRepository;

	@Override
	public ConfigurationId save(final RecommendationConfiguration configuration) {
		final RecommendationConfiguration savedConfig = springDataJpaRecommendationConfigurationRepository.save(configuration);
		return new ConfigurationId(savedConfig.getConfigurationId());
	}

	@Override
	public Optional<RecommendationConfiguration> findBy(final UserId userId) {
		return springDataJpaRecommendationConfigurationRepository.findByUserId(userId);
	}

}

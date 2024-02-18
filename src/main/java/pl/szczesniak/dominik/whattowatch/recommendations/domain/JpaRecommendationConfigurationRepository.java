package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaRecommendationConfigurationRepository implements RecommendationConfigurationRepository {

	private final SpringDataJpaRecommendationConfigurationRepository springDataJpaRecommendationConfigurationRepository;

	@Override
	public ConfigurationId create(final RecommendationConfiguration configuration) {
		final RecommendationConfiguration savedConfig = springDataJpaRecommendationConfigurationRepository.save(configuration);
		return savedConfig.getConfigurationId();
	}

	@Override
	public ConfigurationId update(final RecommendationConfiguration configuration) {
		final RecommendationConfiguration savedConfig = springDataJpaRecommendationConfigurationRepository.save(configuration);
		return savedConfig.getConfigurationId();
	}

	@Override
	public Optional<RecommendationConfiguration> findBy(final UserId userId) {
		return springDataJpaRecommendationConfigurationRepository.findByUserId(userId);
	}

}

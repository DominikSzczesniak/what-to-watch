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
	public ConfigurationId create(final RecommendationConfiguration configuration) {
		final Optional<RecommendationConfiguration> foundConfig = springDataJpaRecommendationConfigurationRepository.findByUserId(configuration.getUserId());
		foundConfig.ifPresent(springDataJpaRecommendationConfigurationRepository::delete);
		return new ConfigurationId(springDataJpaRecommendationConfigurationRepository.save(configuration).getConfigurationId());
	}

	@Override
	public Optional<RecommendationConfiguration> findBy(final UserId userId) {
		return springDataJpaRecommendationConfigurationRepository.findByUserId(userId);
	}

}

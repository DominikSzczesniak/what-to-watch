package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

interface RecommendationConfigurationRepository {

	ConfigurationId create(RecommendationConfiguration configuration);

	ConfigurationId update(RecommendationConfiguration configuration);

	Optional<RecommendationConfiguration> findBy(UserId userId);

	void removeAllBy(UserId userId);

}

@Repository
interface SpringDataJpaRecommendationConfigurationRepository extends RecommendationConfigurationRepository, JpaRepository<RecommendationConfiguration, UserId> {

	@Override
	default void removeAllBy(UserId userId) {
		removeAllByUserId(userId);
	}

	@Override
	default ConfigurationId create(RecommendationConfiguration configuration) {
		return save(configuration).getConfigurationId();
	}

	@Override
	default ConfigurationId update(RecommendationConfiguration configuration) {
		return save(configuration).getConfigurationId();
	}

	@Override
	default Optional<RecommendationConfiguration> findBy(UserId userId) {
		return findByUserId(userId);
	}

	Optional<RecommendationConfiguration> findByUserId(UserId userId);

	void removeAllByUserId(final UserId userId);

}
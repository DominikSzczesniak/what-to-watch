package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Repository
public interface SpringDataJpaRecommendationConfigurationRepository extends JpaRepository<RecommendationConfiguration, UserId> {

	Optional<RecommendationConfiguration> findByUserId(final UserId userId);

}

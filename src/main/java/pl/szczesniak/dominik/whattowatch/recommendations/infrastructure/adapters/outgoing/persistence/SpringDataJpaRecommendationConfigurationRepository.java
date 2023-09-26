package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfiguration;

@Repository
public interface SpringDataJpaRecommendationConfigurationRepository extends JpaRepository<RecommendationConfiguration, Long> {
}

package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendedMovies;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Repository
public interface SpringDataJpaRecommendedMoviesRepository extends JpaRepository<RecommendedMovies, Long> {

	RecommendedMovies findTopByUserIdOrderByDateDesc(UserId userId);

}

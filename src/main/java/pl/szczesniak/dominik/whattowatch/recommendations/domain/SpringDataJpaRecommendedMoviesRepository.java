package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendedMovies;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;

@Repository
interface SpringDataJpaRecommendedMoviesRepository extends JpaRepository<RecommendedMovies, Long> {

	RecommendedMovies findTopByUserIdOrderByCreationDateDesc(UserId userId);

	@Query("SELECT COUNT(movies.id) > 0 FROM RecommendedMovies movies " +
			"WHERE movies.userId = :userId AND movies.creationDate < :endInterval AND movies.endInterval > :startDate")
	boolean existsByUserIdAndStartDateAndEndInterval(UserId userId,
													 LocalDateTime startDate,
													 LocalDateTime endInterval);

}

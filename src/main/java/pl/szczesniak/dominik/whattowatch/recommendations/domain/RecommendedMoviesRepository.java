package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.Optional;

interface RecommendedMoviesRepository {

	RecommendedMoviesId create(RecommendedMovies recommendedMovies);

	Optional<RecommendedMovies> findLatestRecommendedMovies(UserId userId);

	boolean existsByUserIdAndRecommendationDateBetween(UserId userId, LocalDateTime intervalStart, LocalDateTime intervalEnd);

}

@Repository
interface SpringDataJpaRecommendedMoviesRepository extends RecommendedMoviesRepository, JpaRepository<RecommendedMovies, Long> {

	@Override
	default RecommendedMoviesId create(RecommendedMovies recommendedMovies) {
		final RecommendedMovies recommendation = save(recommendedMovies);
		return recommendation.getRecommendedMoviesId();
	}

	@Override
	default Optional<RecommendedMovies> findLatestRecommendedMovies(UserId userId) {
		return findTopByUserIdOrderByCreationDateDesc(userId);
	}

	@Override
	default boolean existsByUserIdAndRecommendationDateBetween(UserId userId, LocalDateTime intervalStart, LocalDateTime intervalEnd) {
		return existsByUserIdAndStartDateAndEndInterval(userId, intervalStart, intervalEnd);
	}

	Optional<RecommendedMovies> findTopByUserIdOrderByCreationDateDesc(UserId userId);

	@Query("SELECT COUNT(movies.id) > 0 FROM RecommendedMovies movies " +
			"WHERE movies.userId = :userId AND movies.creationDate < :endInterval AND movies.endInterval > :startDate")
	boolean existsByUserIdAndStartDateAndEndInterval(UserId userId,
													 LocalDateTime startDate,
													 LocalDateTime endInterval);

}
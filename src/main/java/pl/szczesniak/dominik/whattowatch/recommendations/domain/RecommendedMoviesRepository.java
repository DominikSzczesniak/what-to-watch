package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RecommendedMoviesRepository {

	RecommendedMoviesId create(RecommendedMovies recommendedMovies);

	Optional<RecommendedMovies> findLatestRecommendedMovies(UserId userId);

	boolean existsByUserIdAndRecommendationDateBetween(UserId userId, LocalDateTime intervalStart, LocalDateTime intervalEnd);

}

package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

public interface RecommendedMoviesQueryService {

	Optional<RecommendedMoviesQueryResult> findLatestRecommendedMoviesQueryResult(UserId userId);

}

package pl.szczesniak.dominik.whattowatch.recommendations.query;

import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface RecommendationConfigurationQueryService {

	Optional<RecommendationConfigurationQueryResult> findRecommendationConfigurationQueryResultBy(UserId userId);

	List<UserId> findAllUsersWithRecommendationConfigurations();

}

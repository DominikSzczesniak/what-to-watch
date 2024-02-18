package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface RecommendationQueryService {

	Optional<RecommendationConfigurationRequestResult> findRecommendationConfigurationQueryResultBy(UserId userId);

	List<UserId> findAllUsersWithRecommendationConfigurations();

}

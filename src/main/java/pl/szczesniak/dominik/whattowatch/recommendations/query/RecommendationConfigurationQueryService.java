package pl.szczesniak.dominik.whattowatch.recommendations.query;

import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface RecommendationConfigurationQueryService {

	Optional<RecommendationConfigurationRequestResult> findRecommendationConfigurationQueryResultBy(UserId userId);

	List<UserId> findAllUsersWithRecommendationConfigurations();

}

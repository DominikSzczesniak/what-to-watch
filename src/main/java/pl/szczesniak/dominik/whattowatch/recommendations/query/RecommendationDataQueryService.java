package pl.szczesniak.dominik.whattowatch.recommendations.query;

import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationDataQueryResult;

import java.util.List;

public interface RecommendationDataQueryService {

	List<RecommendationDataQueryResult> findAllRecommendationDataQueryResult();

}

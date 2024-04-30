package pl.szczesniak.dominik.whattowatch.recommendations.query.model;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;

import java.util.Set;

@Value
public class RecommendationDataQueryResult {

	@NonNull Integer userId;

	@NonNull Set<MovieGenre> genres;

//	Integer latestRecommendationVersion;

}

package pl.szczesniak.dominik.whattowatch.recommendations.query.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;

import java.util.Set;

@Value
@EqualsAndHashCode(of = "configurationId")
public class RecommendationConfigurationRequestResult {

	@NonNull Integer userId;

	@NonNull Integer configurationId;

	@NonNull Set<MovieGenre> genres;

}

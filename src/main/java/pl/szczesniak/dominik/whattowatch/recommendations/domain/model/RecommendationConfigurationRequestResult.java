package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Set;

@Value
@EqualsAndHashCode(of = "configurationId")
public class RecommendationConfigurationRequestResult {

	Integer userId;

	Integer configurationId;

	Set<MovieGenre> genres;

}

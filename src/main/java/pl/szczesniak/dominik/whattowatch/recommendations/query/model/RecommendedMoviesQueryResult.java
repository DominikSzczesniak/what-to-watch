package pl.szczesniak.dominik.whattowatch.recommendations.query.model;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class RecommendedMoviesQueryResult {

	@NonNull Integer recommendedMoviesId;

	@NonNull List<MovieInfo> movies;

	@NonNull LocalDateTime creationDate;

	@NonNull LocalDateTime endInterval;

}

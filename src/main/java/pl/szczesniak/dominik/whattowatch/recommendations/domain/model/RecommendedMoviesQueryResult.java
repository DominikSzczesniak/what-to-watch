package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class RecommendedMoviesQueryResult {

	Integer recommendedMoviesId;

	List<MovieInfo> movies;

	LocalDateTime creationDate;

	LocalDateTime endInterval;

}

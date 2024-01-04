package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
public class MovieGenreResponse {

	@NonNull Map<Long, MovieGenre> genres;

}

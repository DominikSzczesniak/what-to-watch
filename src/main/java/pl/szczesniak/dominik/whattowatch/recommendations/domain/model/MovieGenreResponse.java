package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.Value;

import java.util.Map;

@Value
public class MovieGenreResponse {

	Map<Long, MovieGenre> genres;

}

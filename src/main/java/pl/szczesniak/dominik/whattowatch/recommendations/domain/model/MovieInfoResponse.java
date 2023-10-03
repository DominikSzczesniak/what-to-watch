package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.Value;

import java.util.List;

@Value
public class MovieInfoResponse {

	List<MovieInfo> results;

}

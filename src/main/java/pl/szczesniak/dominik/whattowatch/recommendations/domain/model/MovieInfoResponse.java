package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class MovieInfoResponse {

	@NonNull List<MovieInfo> results;

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class MovieInfoResponse {

	private List<MovieInfo> results;

}

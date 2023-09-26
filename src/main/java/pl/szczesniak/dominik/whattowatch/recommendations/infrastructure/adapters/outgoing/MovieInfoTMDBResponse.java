package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import lombok.Data;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;

import java.util.List;

@Data
class MovieInfoTMDBResponse {

	private List<MovieInfo> results;

}

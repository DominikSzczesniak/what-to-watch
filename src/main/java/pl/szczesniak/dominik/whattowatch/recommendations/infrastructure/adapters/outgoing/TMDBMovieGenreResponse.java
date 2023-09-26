package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import lombok.Data;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;

import java.util.List;

@Data
class TMDBMovieGenreResponse {

	private List<MovieGenre> genres;

}

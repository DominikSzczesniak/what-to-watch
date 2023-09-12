package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GenresResponse {

	private List<Genres> genres;

}

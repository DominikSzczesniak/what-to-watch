package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class MovieInfo {

	@JsonProperty("genre_ids")
	private List<Long> genres;
	private Long id;
	private String overview;
	private String title;

}

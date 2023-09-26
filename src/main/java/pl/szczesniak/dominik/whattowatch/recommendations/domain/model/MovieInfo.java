package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class MovieInfo {

	private final List<Long> genreIds;

	private final Long id;

	private final String overview;

	private final String title;

	public MovieInfo(@JsonProperty("genre_ids") final List<Long> genreIds,
					 @JsonProperty("id") final Long id,
					 @JsonProperty("overview") final String overview,
					 @JsonProperty("title") final String title) {
		this.genreIds = requireNonNull(genreIds);
		this.id = requireNonNull(id);
		this.overview = requireNonNull(overview);
		this.title = requireNonNull(title);
	}

}


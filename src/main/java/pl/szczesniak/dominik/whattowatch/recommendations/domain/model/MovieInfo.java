package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class MovieInfo {

	private final Long id;

	private final List<Long> genreIds;

	private final String overview;

	private final String title;

	public MovieInfo(final Long id, final List<Long> genreIds, final String overview, final String title) {
		this.genreIds = requireNonNull(genreIds);
		this.id = requireNonNull(id);
		this.overview = requireNonNull(overview);
		this.title = requireNonNull(title);
	}

}


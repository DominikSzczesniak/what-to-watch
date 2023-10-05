package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Getter
@ToString
@EqualsAndHashCode
public class MovieInfo {

	private final String title;

	private final String overview;

	private final List<MovieGenre> genres;

	public MovieInfo(final List<MovieGenre> genres, final String overview, final String title) {
		this.genres = requireNonNull(genres);
		this.overview = requireNonNull(overview);
		this.title = requireNonNull(title);
	}

}


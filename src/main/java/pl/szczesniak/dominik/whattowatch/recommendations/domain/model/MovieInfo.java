package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Getter
@ToString
@EqualsAndHashCode
public class MovieInfo {

	private final List<MovieGenre> genres;

	private final String overview;

	private final String title;

	public MovieInfo(@NonNull final List<MovieGenre> genres, @NonNull final String overview, @NonNull final String title) {
		this.genres = requireNonNull(genres);
		this.overview = requireNonNull(overview);
		this.title = requireNonNull(title);
	}

}


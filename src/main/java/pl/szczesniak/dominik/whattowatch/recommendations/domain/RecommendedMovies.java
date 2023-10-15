package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class RecommendedMovies {

	@Setter(AccessLevel.PACKAGE)
	RecommendedMoviesId id;

	List<MovieInfo> movies;

	LocalDateTime date;

	RecommendedMovies(final List<MovieInfo> movies) {
		this.movies = movies;
		this.date = LocalDateTime.now();
	}
}

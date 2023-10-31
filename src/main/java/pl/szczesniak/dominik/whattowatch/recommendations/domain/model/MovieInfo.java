package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MovieInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer movieInfoId;

	@ElementCollection
	private List<MovieGenre> genres;

	private String overview;

	private String title;

	public MovieInfo(@NonNull final List<MovieGenre> genres, @NonNull final String overview, @NonNull final String title) {
		this.genres = requireNonNull(genres);
		this.overview = requireNonNull(overview);
		this.title = requireNonNull(title);
	}

}


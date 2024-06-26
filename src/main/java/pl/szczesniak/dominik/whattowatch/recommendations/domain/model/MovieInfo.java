package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@EqualsAndHashCode(of = {"externalId"})
public class MovieInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer movieInfoId;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<MovieGenre> genres;

	@Column(length = 1024)
	private String overview;

	private String title;

	private Integer externalId;

	@Enumerated(EnumType.STRING)
	private MovieInfoApis externalApi;

	public MovieInfo(@NonNull final List<MovieGenre> genres,
					 @NonNull final String overview,
					 @NonNull final String title,
					 @NonNull final Integer externalId,
					 @NonNull final MovieInfoApis externalApi) {
		this.genres = requireNonNull(genres);
		this.overview = requireNonNull(overview);
		this.title = requireNonNull(title);
		this.externalId = externalId;
		this.externalApi = externalApi;
	}

}


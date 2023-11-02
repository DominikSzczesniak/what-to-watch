package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public class RecommendedMovies {

	@Id
	@Setter(AccessLevel.PACKAGE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@AttributeOverride(name = "value", column = @Column(name = "recommended_movies_id"))
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private List<MovieInfo> movies;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	private LocalDateTime date;

	RecommendedMovies(final List<MovieInfo> movies, final UserId userId) {
		this.movies = movies;
		this.date = LocalDateTime.now();
		this.userId = userId;
	}

}

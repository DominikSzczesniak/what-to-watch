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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"recommendedMoviesId"})
public class RecommendedMovies {

	@Id
	@Setter(AccessLevel.PACKAGE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recommendedMoviesId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private List<MovieInfo> movies;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	private LocalDateTime creationDate;

	private LocalDateTime endInterval;

	RecommendedMovies(final List<MovieInfo> movies, final UserId userId) {
		this.movies = movies;
		this.creationDate = LocalDateTime.now();
		this.userId = userId;
		setEndInterval();
	}

	private void setEndInterval() {
		this.endInterval = LocalDateTime.now()
				.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY))
				.withHour(23)
				.withMinute(59)
				.withSecond(59)
				.withNano(999999999);
	}

}

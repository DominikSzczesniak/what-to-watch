package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
class RecommendedMovies extends BaseEntity {

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id")
	private List<MovieInfo> movies;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	private LocalDateTime creationDate;

	private LocalDateTime endInterval;

	RecommendedMovies(@NonNull final List<MovieInfo> movies, @NonNull final UserId userId) {
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

	RecommendedMoviesId getRecommendedMoviesId() {
		return new RecommendedMoviesId(getId());
	}

//	@Override
//	public String toString() {
//		return "RecommendedMovies{" +
//				"id=" + getId() +
//				", userId=" + userId +
//				", creationDate=" + creationDate +
//				", endInterval=" + endInterval +
//				'}';
//	}

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Entity
@Getter
@Table(name = "app_watchedMovie")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"movieId"})
public class WatchedMovie {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "movieId_value"))
	private MovieId movieId;

	@AttributeOverride(name = "value", column = @Column(name = "movieTitle_value"))
	private MovieTitle title;

	@AttributeOverride(name = "value", column = @Column(name = "userId_value"))
	private UserId userId;

	WatchedMovie(final MovieId movieId, final MovieTitle title, final UserId userId) {
		this.movieId = movieId;
		this.title = title;
		this.userId = userId;
	}
}

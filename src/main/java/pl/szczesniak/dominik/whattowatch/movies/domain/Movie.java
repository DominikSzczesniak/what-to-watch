package pl.szczesniak.dominik.whattowatch.movies.domain;

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
@Table(name = "app_movie")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"movieId"})
public class Movie {

	@EmbeddedId
	@Getter
	private MovieId movieId;

	@Getter
	private UserId userId;

	@Getter
	private MovieTitle title;

	Movie(final MovieId movieId, final UserId userId, final MovieTitle title) {
		this.movieId = movieId;
		this.userId = userId;
		this.title = title;
	}

	public static Movie recreate(final MovieId movieId, final MovieTitle title, final UserId userId) {
		return new Movie(movieId, userId, title);
	}

	WatchedMovie markAsWatched() {
		return new WatchedMovie(movieId, title, userId);
	}

	public void update(final MovieTitle title) {
		this.title = title;
	}

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@ToString
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = {"movieId"})
public class Movie {

	@Getter
	private final MovieId movieId;
	@Getter
	private MovieTitle title;
	@Getter
	private final UserId userId;

	public static Movie recreate(final MovieId movieId, final MovieTitle title, final UserId userId) {
		return new Movie(movieId, title, userId);
	}

	WatchedMovie markAsWatched() {
		return new WatchedMovie(movieId, title, userId);
	}
}

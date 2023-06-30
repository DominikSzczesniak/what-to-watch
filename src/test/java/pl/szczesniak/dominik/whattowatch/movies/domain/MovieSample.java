package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class MovieSample {

	@Builder
	private static Movie build(final MovieId movieId, final MovieTitle movieTitle, final UserId userId) {
		return new Movie(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(movieTitle).orElse(createAnyMovieTitle())
		);
	}

}

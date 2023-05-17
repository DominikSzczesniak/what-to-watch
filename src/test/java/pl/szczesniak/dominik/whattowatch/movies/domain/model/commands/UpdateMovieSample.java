package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class UpdateMovieSample {

	@Builder
	private static UpdateMovie build(final MovieId movieId, final UserId userId, final MovieTitle movieTitle) {
		return UpdateMovie.builder(
						ofNullable(movieId).orElse(createAnyMovieId()),
						ofNullable(userId).orElse(createAnyUserId()),
						ofNullable(movieTitle).orElse(createAnyMovieTitle()))
				.build();
	}

}
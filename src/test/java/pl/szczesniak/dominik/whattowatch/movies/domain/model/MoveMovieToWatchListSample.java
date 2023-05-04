package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class MoveMovieToWatchListSample {

	@Builder
	private static MoveMovieToWatchedMoviesList build(final MovieId movieId, final UserId userId) {
		return new MoveMovieToWatchedMoviesList(
				ofNullable(movieId).orElse(createAnyMovieId()),
				ofNullable(userId).orElse(createAnyUserId())
		);
	}

}

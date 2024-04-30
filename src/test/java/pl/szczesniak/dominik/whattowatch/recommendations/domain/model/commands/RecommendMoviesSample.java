package pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Collections;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class RecommendMoviesSample {

	@Builder
	private static RecommendMovies build(final UserId userId, final Set<MovieGenre> genres) {
		return new RecommendMovies(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(genres).orElse(Collections.emptySet())
		);
	}

}

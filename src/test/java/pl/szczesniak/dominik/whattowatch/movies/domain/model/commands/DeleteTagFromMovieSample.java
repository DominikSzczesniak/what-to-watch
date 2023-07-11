package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TagIdSample.createAnyTagId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class DeleteTagFromMovieSample {

	@Builder
	private static DeleteTagFromMovie build(final UserId userId, final MovieId movieId, final TagId tagId) {
		return new DeleteTagFromMovie(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(movieId).orElse(createAnyMovieId()),
				ofNullable(tagId).orElse(createAnyTagId())
		);
	}

}

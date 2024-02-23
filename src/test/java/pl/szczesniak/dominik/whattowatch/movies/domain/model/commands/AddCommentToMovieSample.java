package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentSample.createAnyComment;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class AddCommentToMovieSample {

	@Builder
	private static AddCommentToMovie build(final UserId userId, final MovieId movieId, final String comment) {
		return new AddCommentToMovie(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(movieId).orElse(createAnyMovieId()),
				ofNullable(comment).orElse(createAnyComment())
		);
	}

}

package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentIdSample.createAnyCommentId;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class DeleteCommentFromMovieSample {

	@Builder
	private static DeleteCommentFromMovie build(final UserId userId, final MovieId movieId, final CommentId commentId) {
		return new DeleteCommentFromMovie(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(movieId).orElse(createAnyMovieId()),
				ofNullable(commentId).orElse(createAnyCommentId())
		);
	}

}

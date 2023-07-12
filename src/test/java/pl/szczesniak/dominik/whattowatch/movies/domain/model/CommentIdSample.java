package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import java.util.UUID;

public class CommentIdSample {

	public static CommentId createAnyCommentId() {
		return new CommentId(UUID.randomUUID());
	}

}

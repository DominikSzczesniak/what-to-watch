package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

@Value
public class MovieCommentQueryResult {

	CommentId commentId;

	String text;

}

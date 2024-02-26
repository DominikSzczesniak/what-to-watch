package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class MovieCommentQueryResult {

	@NonNull String commentId;

	@NonNull String text;

}

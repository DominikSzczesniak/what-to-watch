package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Set;

@Value
public class MovieQueryResult {

	@NonNull Integer movieId;

	@NonNull String title;

	@NonNull Set<MovieCommentQueryResult> comments;

	@NonNull Set<MovieTagQueryResult> tags;

}

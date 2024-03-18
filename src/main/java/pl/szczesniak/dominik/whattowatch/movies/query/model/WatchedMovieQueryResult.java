package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class WatchedMovieQueryResult {

	@NonNull Integer movieId;

	@NonNull String title;

	@NonNull Integer userId;

}

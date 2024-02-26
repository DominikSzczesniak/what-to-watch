package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class MovieInListQueryResult {

	@NonNull Integer movieId;

	@NonNull String title;

}

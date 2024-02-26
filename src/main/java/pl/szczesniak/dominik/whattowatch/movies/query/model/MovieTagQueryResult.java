package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class MovieTagQueryResult {

	@NonNull String tagId;

	@NonNull String label;

	@NonNull Integer userId;

}

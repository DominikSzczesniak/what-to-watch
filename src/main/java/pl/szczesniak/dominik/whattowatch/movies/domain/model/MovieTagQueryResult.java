package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

@Value
public class MovieTagQueryResult {

	String tagId;

	String label;

	Integer userId;

}

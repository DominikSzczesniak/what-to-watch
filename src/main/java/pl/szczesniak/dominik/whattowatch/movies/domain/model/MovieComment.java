package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

import java.util.UUID;

@Value
public class MovieComment {

	UUID commentId;

	String value;

}

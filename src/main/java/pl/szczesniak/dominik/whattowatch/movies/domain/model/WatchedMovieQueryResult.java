package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

@Value
public class WatchedMovieQueryResult {

	Integer movieId;

	String title;

	Integer userId;

}

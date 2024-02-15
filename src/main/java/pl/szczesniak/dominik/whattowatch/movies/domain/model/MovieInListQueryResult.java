package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

import java.util.Set;

@Value
public class MovieInListQueryResult {

	MovieId movieId;

	MovieTitle title;

	Set<MovieTagQueryResult> tags;

}

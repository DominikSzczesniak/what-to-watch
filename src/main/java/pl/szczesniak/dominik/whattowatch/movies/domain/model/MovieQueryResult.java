package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

import java.util.Set;

@Value
public class MovieQueryResult {

	MovieId movieId;

	MovieTitle title;

	Set<MovieComment> comments;

	Set<MovieTagQueryResult> tags;

}

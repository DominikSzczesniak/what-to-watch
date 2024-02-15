package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class WatchedMovieQueryResult {

	MovieId movieId;

	MovieTitle title;

	UserId userId;

}

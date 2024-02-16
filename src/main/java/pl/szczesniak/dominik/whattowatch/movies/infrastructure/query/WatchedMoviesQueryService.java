package pl.szczesniak.dominik.whattowatch.movies.infrastructure.query;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface WatchedMoviesQueryService {

	List<WatchedMovieQueryResult> getWatchedMovies(UserId userId);

}

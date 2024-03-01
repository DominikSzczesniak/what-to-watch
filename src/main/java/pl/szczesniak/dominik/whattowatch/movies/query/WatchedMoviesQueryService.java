package pl.szczesniak.dominik.whattowatch.movies.query;

import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedWatchedMovies;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

public interface WatchedMoviesQueryService {

	PagedWatchedMovies getWatchedMovies(UserId userId, Integer page, Integer moviesPerPage);

}

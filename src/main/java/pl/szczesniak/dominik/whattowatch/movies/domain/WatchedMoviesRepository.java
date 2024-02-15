package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface WatchedMoviesRepository {

	void add(WatchedMovie watchedMovie);

}

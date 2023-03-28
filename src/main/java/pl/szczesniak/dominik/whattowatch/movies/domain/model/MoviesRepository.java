package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface MoviesRepository {

    int nextMovieId();
    void save(final Movie movie);
    List<Movie> findAll(final UserId userId);
    void removeMovie(final Movie movie);

}

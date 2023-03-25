package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface MoviesRepository {

    MovieId nextMovieId();
    void save(Movie movie);
    List<Movie> findAll(UserId userId);
    void removeMovie(Movie movie);

}

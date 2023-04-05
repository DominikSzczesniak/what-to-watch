package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface MoviesRepository {

    MovieId nextMovieId();
    void save(final Movie movie);
    List<Movie> findAll(final UserId userId);
    void removeMovie(final MovieId movieId);

}
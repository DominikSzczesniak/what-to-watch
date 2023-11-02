package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MoviesRepository {

	void create(Movie movie);

	void update(Movie movie);

	List<Movie> findAll(UserId userId);

	void removeMovie(MovieId movieId, UserId userId);

	Optional<Movie> findBy(MovieId movieId, UserId userId);

	List<Movie> findAllMoviesByTagIds(List<MovieTagId> tags, UserId userId);

}
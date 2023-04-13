package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface MoviesRepository {

	MovieId nextMovieId();
	void save(Movie movie);
	List<Movie> findAll(UserId userId);
	void removeMovie(MovieId movieId, UserId userId);
	Movie getMovie(MovieId movieId, UserId userId);
	Optional<Movie> findBy(MovieId movieId, UserId userId);

}

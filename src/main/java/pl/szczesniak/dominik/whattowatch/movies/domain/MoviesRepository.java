package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface MoviesRepository {

	void create(Movie movie);

	void update(Movie movie);

	List<Movie> findAll(UserId userId);

	void removeMovie(MovieId movieId, UserId userId);

	Optional<Movie> findBy(MovieId movieId, UserId userId);

	List<Movie> findAllByTagLabel(TagLabel tagLabel, UserId userId);

}
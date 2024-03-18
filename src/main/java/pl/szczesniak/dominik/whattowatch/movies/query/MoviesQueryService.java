package pl.szczesniak.dominik.whattowatch.movies.query;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface MoviesQueryService {

	List<MovieInListQueryResult> getMoviesToWatch(UserId userId);

	Optional<MovieQueryResult> findMovieQueryResult(MovieId movieId, UserId userId);

	Optional<MovieTagQueryResult> getTagByTagId(MovieTagId tagId);

	List<MovieInListQueryResult> getMoviesByTags(List<MovieTagId> tags, UserId userId);

	List<MovieTagQueryResult> getMovieTagsByUserId(Integer userId);

}

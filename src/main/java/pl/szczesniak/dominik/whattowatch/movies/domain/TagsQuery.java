package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;

import java.util.List;
import java.util.Optional;

public interface TagsQuery {

	List<MovieTag> findByUserId(Integer userId);

	Optional<MovieTag> findTagByTagId(String tagId);

	List<MovieId> findAllMoviesByTagId(String tagId, Integer userId);

}

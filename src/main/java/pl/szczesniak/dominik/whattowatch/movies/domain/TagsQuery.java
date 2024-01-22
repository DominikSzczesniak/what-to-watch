package pl.szczesniak.dominik.whattowatch.movies.domain;

import java.util.List;
import java.util.Optional;

public interface TagsQuery {

	List<MovieTag> findByUserId(Integer userId);

	Optional<MovieTag> findTagByTagId(String tagId);

}

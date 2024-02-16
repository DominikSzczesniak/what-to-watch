package pl.szczesniak.dominik.whattowatch.movies.domain;

import java.util.Optional;

public interface TagsQuery {

	Optional<MovieTag> findTagByTagId(String tagId);

}

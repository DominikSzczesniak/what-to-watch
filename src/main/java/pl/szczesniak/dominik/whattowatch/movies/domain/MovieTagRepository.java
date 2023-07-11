package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface MovieTagRepository {

	void add(MovieTag movieTag);

	List<MovieTag> findAllBy(UserId userId);

	void deleteTag(TagId tagId, UserId userId);

}

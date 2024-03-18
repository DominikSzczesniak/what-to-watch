package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;

import java.util.Optional;
import java.util.UUID;

interface TagsRepository {

	Optional<MovieTag> findTagByTagId(String tagId);

}

@Repository
interface SpringDataJpaMovieTagsRepository extends TagsRepository, JpaRepository<MovieTag, MovieTagId> {

	@Override
	default Optional<MovieTag> findTagByTagId(String tagId) {
		return findById(new MovieTagId(UUID.fromString(tagId)));
	}
}
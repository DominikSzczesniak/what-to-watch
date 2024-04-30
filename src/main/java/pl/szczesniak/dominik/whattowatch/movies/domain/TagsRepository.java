package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;
import java.util.UUID;

interface TagsRepository {

	Optional<MovieTag> findTagByTagId(String tagId);

	void deleteAllMovieTagsBy(UserId userId);
}

@Repository
interface SpringDataJpaMovieTagsRepository extends TagsRepository, JpaRepository<MovieTag, MovieTagId> {

	@Override
	default void deleteAllMovieTagsBy(UserId userId) {
		deleteAllByUserId(userId);
	}

	@Override
	default Optional<MovieTag> findTagByTagId(String tagId) {
		return findById(new MovieTagId(UUID.fromString(tagId)));
	}

	void deleteAllByUserId(final UserId userId);

}
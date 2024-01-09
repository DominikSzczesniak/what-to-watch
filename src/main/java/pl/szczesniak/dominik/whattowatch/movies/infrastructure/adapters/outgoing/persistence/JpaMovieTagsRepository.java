package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.MovieTag;
import pl.szczesniak.dominik.whattowatch.movies.domain.TagsQuery;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaMovieTagsRepository implements TagsQuery {

	private final SpringDataJpaMovieTagsRepository springDataJpaMovieTagsRepository;

	@Override
	public List<MovieTag> findByUserId(final Integer userId) {
		return springDataJpaMovieTagsRepository.findByUserId(new UserId(userId));
	}

	@Override
	public Optional<MovieTag> findTagByTagId(final String tagId) {
		return springDataJpaMovieTagsRepository.findById(new MovieTagId(UUID.fromString(tagId)));
	}

}
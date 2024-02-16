package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaMovieTagsRepository implements TagsQuery {

	private final SpringDataJpaMovieTagsRepository springDataJpaMovieTagsRepository;

	@Override
	public Optional<MovieTag> findTagByTagId(final String tagId) {
		return springDataJpaMovieTagsRepository.findById(new MovieTagId(UUID.fromString(tagId)));
	}

}

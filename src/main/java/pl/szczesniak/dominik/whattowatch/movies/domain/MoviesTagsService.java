package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class MoviesTagsService {

	private final MoviesToWatchRepository moviesToWatchRepository;
	private final TagsRepository tagsRepository;

	MovieTagId addTagToMovie(final AddTagToMovie command) {
		final MovieTagId tagId = command.getTagId().orElse(new MovieTagId(UUID.randomUUID()));
		final Optional<MovieTag> movieTag = checkMovieTagBelongsToUser(command.getUserId(), tagId);
		final MovieTagLabel tagLabel = movieTag.map(MovieTag::getLabel).orElse(command.getTagLabel());

		final Movie movie = moviesToWatchRepository.getMovie(command.getMovieId(), command.getUserId());
		movie.addTag(tagId, tagLabel, command.getUserId());

		moviesToWatchRepository.update(movie);
		return tagId;
	}

	private Optional<MovieTag> checkMovieTagBelongsToUser(final UserId userId, final MovieTagId tagId) {
		final Optional<MovieTag> tagByTagId = tagsRepository.findTagByTagId(tagId);
		if (tagByTagId.isPresent() && !tagByTagId.get().getUserId().equals(userId)) {
			throw new ObjectDoesNotExistException("MovieTag does not belong to user");
		}
		return tagByTagId;
	}

	void deleteTagFromMovie(final DeleteTagFromMovie command) {
		final Movie movie = moviesToWatchRepository.getMovie(command.getMovieId(), command.getUserId());
		movie.deleteTag(command.getTagId());
		moviesToWatchRepository.update(movie);
	}

}

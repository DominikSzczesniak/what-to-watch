package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabelSample.createAnyTagLabel;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class AddTagToMovieSample {

	@Builder
	private static AddTagToMovie build(final UserId userId, final MovieId movieId, final MovieTagLabel tagLabel, final MovieTagId tagId) {
		return new AddTagToMovie(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(movieId).orElse(createAnyMovieId()),
				ofNullable(tagLabel).orElse(createAnyTagLabel()),
				tagId
		);
	}
}

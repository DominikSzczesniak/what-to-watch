package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Value
public class AddTagToMovie {

	@NonNull UserId userId;
	@NonNull MovieId movieId;
	@NonNull MovieTagLabel tagLabel;
	MovieTagId tagId;

	public Optional<MovieTagId> getTagId() {
		return Optional.ofNullable(tagId);
	}
}

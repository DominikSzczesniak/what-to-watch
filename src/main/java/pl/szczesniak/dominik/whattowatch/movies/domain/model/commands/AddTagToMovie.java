package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Value
public class AddTagToMovie {

	@NonNull UserId userId;
	@NonNull MovieId movieId;
	@NonNull TagLabel tagLabel;
	TagId tagId;

	public Optional<TagId> getTagId() {
		return Optional.ofNullable(tagId);
	}
}

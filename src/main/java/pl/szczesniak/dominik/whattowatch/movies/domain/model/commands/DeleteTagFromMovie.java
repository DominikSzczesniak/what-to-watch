package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class DeleteTagFromMovie {

	@NonNull UserId userId;
	@NonNull MovieId movieId;
	@NonNull TagId tagId;

}

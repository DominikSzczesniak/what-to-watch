package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class GetWatchedMoviesToWatch {

	@NonNull UserId userId;

	@NonNull Integer page;

	@NonNull Integer moviesPerPage;

}

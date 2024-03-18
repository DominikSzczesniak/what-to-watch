package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class GetMoviesToWatch {

	@NonNull UserId userId;

	@NonNull Integer page;

	@NonNull Integer moviesPerPage;

}

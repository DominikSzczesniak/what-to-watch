package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class MoveMovieToWatchList {

	@NonNull MovieId movieId;
	@NonNull UserId userId;

}

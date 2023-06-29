package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;

@Value
public class MoveMovieToWatchedMoviesList {

	@NonNull MovieId movieId;
	@NonNull Long userId;

}

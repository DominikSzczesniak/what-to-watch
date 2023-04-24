package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@ToString
@RequiredArgsConstructor
public class WatchedMovie {

	@Getter
	private final MovieId movieId;
	@Getter
	private final MovieTitle title;
	@Getter
	private final UserId userId;

}
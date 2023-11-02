package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
@RestController
public class MoveMovieToWatchedListController {

	private final MoviesFacade moviesFacade;

	@PostMapping("/api/movies/{movieId}/watched")
	public void moveMovieToWatchedList(@PathVariable Integer movieId, @RequestHeader("userId") Integer userId) {
		moviesFacade.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(new MovieId(movieId), new UserId(userId)));
	}

}

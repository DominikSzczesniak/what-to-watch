package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FindWatchedMoviesController {

	private final MoviesFacade moviesFacade;

	@GetMapping("/api/movies/watched")
	public List<WatchedMovieDto> findWatchedMovies(@RequestHeader("userId") Integer userId) {
		return moviesFacade.getWatchedMovies(new UserId(userId)).stream()
				.map(movie -> new WatchedMovieDto(movie.getTitle(), movie.getMovieId(), movie.getUserId()))
				.toList();
	}

	@Value
	private static class WatchedMovieDto {
		String title;
		Integer movieId;
		Integer userId;
	}

}

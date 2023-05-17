package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FindWatchedMoviesController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies/watched")
	public List<MovieDto> findWatchedMovies(@RequestHeader("userId") Integer userId) {
		return moviesService.getWatchedMovies(new UserId(userId)).stream()
				.map(movie -> new MovieDto(movie.getTitle().getValue(), movie.getMovieId().getValue(), movie.getUserId().getValue()))
				.toList();
	}

	@Value
	private static class MovieDto {
		String title;
		Integer movieId;
		Integer userId;
	}

}

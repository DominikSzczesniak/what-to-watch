package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class controller {

	private final MovieInfoApi movieInfoApi;

	@GetMapping("/api/genres")
	public MovieGenreResponse findAllGenres(@RequestHeader("userId") final Integer userId) {
		return movieInfoApi.getGenres();
	}

	@GetMapping("/api/movies/info")
	public MovieInfoResponse findAllMoviesInfo(@RequestHeader("userId") final Integer userId) {
		return movieInfoApi.getPopularMovies();
	}

	@GetMapping("/api/movies/genre")
	public MovieInfoResponse findMoviesByGenreId(@RequestHeader("userId") final Integer userId, @RequestParam List<Long> genreId) {
		return movieInfoApi.getMoviesByGenre(genreId);
	}

}

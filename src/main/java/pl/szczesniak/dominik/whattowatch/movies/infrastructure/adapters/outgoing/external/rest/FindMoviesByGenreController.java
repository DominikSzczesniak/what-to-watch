package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfo;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfoService;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class FindMoviesByGenreController {

	private final MovieInfoService movieInfoService;

	@GetMapping("/api/movies/info/{genreId}")
	public Flux<MovieInfo> getPopularMovies(@PathVariable final Long genreId) {
		return movieInfoService.getMoviesByGenre(genreId);
	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.Genre;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.GenresResponse;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.TMDBMovieInfoProvider;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class GetGenresController {

	private final TMDBMovieInfoProvider TMDBMovieInfoProvider;

	@GetMapping("/api/genres")
	public GenresResponse getGenres() {
		return TMDBMovieInfoProvider.getGenres();
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.movieinfoapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetMovieGenresController {

	private final RecommendationService service;

	@GetMapping("/api/genres")
	public ResponseEntity<List<MovieGenre>> getMovieGenres() {
		final List<MovieGenre> genres = service.getMovieGenres().getGenres().values().stream().toList();
		return ResponseEntity.status(200).body(genres);
	}

}

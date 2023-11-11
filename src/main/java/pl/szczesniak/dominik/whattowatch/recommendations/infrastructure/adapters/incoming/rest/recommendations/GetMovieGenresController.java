package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetMovieGenresController {

	private final RecommendationService service;

	@GetMapping("/api/genres")
	public ResponseEntity<MovieGenresDto> getMovieGenres() {
		final List<String> genres = service.getMovieGenres().stream().map(Enum::toString).toList();
		final MovieGenresDto movieGenresDto = new MovieGenresDto(genres);
		return ResponseEntity.status(200).body(movieGenresDto);
	}

	@Value
	private static class MovieGenresDto {
		List<String> genresNames;
	}

}

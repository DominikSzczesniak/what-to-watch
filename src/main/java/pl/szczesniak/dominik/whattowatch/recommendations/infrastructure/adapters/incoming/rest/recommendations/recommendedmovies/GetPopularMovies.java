package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;

import java.util.List;

import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies.MovieInfoDto.mapToMovieInfoDto;

@RestController
@RequiredArgsConstructor
public class GetPopularMovies {

	private final RecommendationService service;

	@GetMapping("/api/recommendations/popular")
	public ResponseEntity<RecommendedMoviesDto> getPopularMovies() {
		final List<MovieInfo> movies = service.recommendPopularMovies().getResults();

		final RecommendedMoviesDto recommendedMoviesDto = new RecommendedMoviesDto(mapToMovieInfoDto(movies));

		return ResponseEntity.status(200).body(recommendedMoviesDto);
	}

	@Value
	private static class RecommendedMoviesDto {

		List<MovieInfoDto> movieInfos;

	}

}

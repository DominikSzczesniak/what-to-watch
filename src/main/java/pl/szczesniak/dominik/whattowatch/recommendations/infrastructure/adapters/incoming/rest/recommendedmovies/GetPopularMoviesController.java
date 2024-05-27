package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;

import java.util.List;

import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies.MovieInfoDto.mapToMovieInfoDto;

@RestController
@RequiredArgsConstructor
public class GetPopularMoviesController {

	private final RecommendationFacade facade;

	@GetMapping("/api/recommendations/popular")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<RecommendedMoviesDto> getPopularMovies() {
		final List<MovieInfo> movies = facade.recommendPopularMovies().getResults();

		final RecommendedMoviesDto recommendedMoviesDto = new RecommendedMoviesDto(mapToMovieInfoDto(movies));

		return ResponseEntity.status(200).body(recommendedMoviesDto);
	}

	@Value
	private static class RecommendedMoviesDto {

		List<MovieInfoDto> movieInfos;

	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendedMovies;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.List;

import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies.MovieInfoDto.mapToMovieInfoDto;

@RequiredArgsConstructor
@RestController
public class GetLatestRecommendedMovies {

	private final RecommendationService service;

	@GetMapping("/api/recommendations")
	public ResponseEntity<RecommendedMoviesDto> getLatestRecommendedMovies(@RequestHeader("userId") final Integer userId) {
		final RecommendedMovies recommendedMovies = service.findLatestRecommendedMovies(new UserId(userId));

		final RecommendedMoviesDto recommendedMoviesDto = new RecommendedMoviesDto(
				mapToMovieInfoDto(recommendedMovies.getMovies()),
				recommendedMovies.getCreationDate(),
				recommendedMovies.getEndInterval()
		);

		return ResponseEntity.status(200).body(recommendedMoviesDto);
	}


	@Value
	private static class RecommendedMoviesDto {

		List<MovieInfoDto> movieInfos;
		LocalDateTime creationDate;
		LocalDateTime endInterval;

	}

}

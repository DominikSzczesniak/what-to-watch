package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.List;

import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies.MovieInfoDto.mapToMovieInfoDto;

@RequiredArgsConstructor
@RestController
public class GetLatestRecommendedMoviesController {

	private final RecommendationFacade facade;

	@GetMapping("/api/recommendations/latest")
	public ResponseEntity<?> getLatestRecommendedMovies(@RequestHeader("userId") final Integer userId) {
		try {
			final RecommendedMoviesQueryResult recommendedMovies = facade.getLatestRecommendedMovies(new UserId(userId));
			final RecommendedMoviesDto recommendedMoviesDto = new RecommendedMoviesDto(
					mapToMovieInfoDto(recommendedMovies.getMovies()),
					recommendedMovies.getCreationDate(),
					recommendedMovies.getEndInterval()
			);

			return ResponseEntity.status(200).body(recommendedMoviesDto);
		} catch (ObjectDoesNotExistException e) {
			return ResponseEntity.status(204).body(e.getMessage());
		}
	}

	@Value
	private static class RecommendedMoviesDto {

		List<MovieInfoDto> movieInfos;
		LocalDateTime creationDate;
		LocalDateTime endInterval;

	}

}

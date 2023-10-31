package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendingmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendedMovies;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class GetLatestRecommendedMovies {

	private final RecommendationService service;

	@GetMapping("/api/recommendations/{userId}")
	public ResponseEntity<List<MovieInfoDto>> getLatestRecommendedMovies(@PathVariable final Integer userId) {
		final List<MovieInfo> movies = service.findLatestRecommendedMovies(new UserId(userId)).getMovies();

		final List<MovieInfoDto> movieDtos = new ArrayList<>();

		for (MovieInfo movieInfo : movies) {
			final List<String> genres = movieInfo.getGenres().stream()
					.map(Enum::name)
					.collect(Collectors.toList());
			movieDtos.add(new MovieInfoDto(movieInfo.getTitle(), movieInfo.getOverview(), genres));
		}
		return ResponseEntity.status(200).body(movieDtos);
	}

	@Value
	private static class MovieInfoDto {
		String title;
		String overview;
		List<String> genres;
	}

}

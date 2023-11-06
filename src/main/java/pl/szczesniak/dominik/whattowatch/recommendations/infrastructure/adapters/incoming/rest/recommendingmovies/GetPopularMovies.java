package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendingmovies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetPopularMovies {

	private final RecommendationService service;

	@GetMapping("/api/recommendations/popular")
	public ResponseEntity<List<MovieInfoDto>> getPopularMovies() {
		final List<MovieInfo> movies = service.recommendPopularMovies().getResults();

		final List<MovieInfoDto> popularMoviesDto = mapToMovieInfoDto(movies);

		return ResponseEntity.status(200).body(popularMoviesDto);
	}

	private static List<MovieInfoDto> mapToMovieInfoDto(final List<MovieInfo> movies) {
		final List<MovieInfoDto> movieDtos = new ArrayList<>();
		for (MovieInfo movieInfo : movies) {
			final List<String> genres = movieInfo.getGenres().stream()
					.map(Enum::name)
					.collect(Collectors.toList());
			movieDtos.add(new MovieInfoDto(movieInfo.getTitle(), movieInfo.getOverview(), genres));
		}
		return movieDtos;
	}

	@Value
	private static class MovieInfoDto {
		String title;
		String overview;
		List<String> genres;
	}

}

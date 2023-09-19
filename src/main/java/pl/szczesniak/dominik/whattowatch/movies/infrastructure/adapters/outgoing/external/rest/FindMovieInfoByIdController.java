package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfo;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.TMDBMovieInfoProvider;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FindMovieInfoByIdController {

	private final TMDBMovieInfoProvider TMDBMovieInfoProvider;

	@GetMapping("/api/movies/info/{movieId}")
	public Mono<MovieInfoDto> getMovieInfoByMovieId(@PathVariable final Long movieId) {
		final Mono<MovieInfo> movie = TMDBMovieInfoProvider.findMovieById(movieId);
		return movie.map(movieInfo -> new MovieInfoDto(
				movieInfo.getId(),
				movieInfo.getTitle(),
				movieInfo.getOverview(),
				movieInfo.getGenres()
		));
	}

	@Data
	@AllArgsConstructor
	private static class MovieInfoDto {

		private Long id;
		private String title;
		private String overview;
		private List<Long> genres;

	}

}

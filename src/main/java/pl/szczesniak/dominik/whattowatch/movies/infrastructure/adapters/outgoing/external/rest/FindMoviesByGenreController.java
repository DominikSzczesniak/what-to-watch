package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.TMDBMovieInfoProvider;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindMoviesByGenreController {

	private final TMDBMovieInfoProvider TMDBMovieInfoProvider;

	@GetMapping("/api/movies/info/genre/{genreId}")
	public List<MovieInfoDto> getMoviesByGenre(@PathVariable final Long genreId) {
		MovieInfoResponse movies = TMDBMovieInfoProvider.getMoviesByGenre(genreId);
//		return movies.map(movieInfo -> new MovieInfoDto(
//				movieInfo.getId(),
//				movieInfo.getTitle(),
//				movieInfo.getOverview(),
//				movieInfo.getGenres()
//				));
		return movies.getResults().stream().map(movieInfo -> new MovieInfoDto(
						movieInfo.getId(),
						movieInfo.getTitle(),
						movieInfo.getOverview(),
						movieInfo.getGenres()
				))
				.collect(Collectors.toList());
	}

	@Data
	@AllArgsConstructor
	private static class MovieInfoDto {

		private Long id;
		private String title;
		private String overview;
		private List<Long> genre_ids;

	}
}

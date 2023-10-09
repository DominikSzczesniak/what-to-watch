package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindAllMoviesByTagIdController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies/tags/filter")
	public ResponseEntity<?> findAllMoviesByTagId(@RequestHeader Integer userId, @RequestBody TagsIdsDto tags) {
		final List<MovieTagId> list = tags.getTags().stream()
				.map(tag -> new MovieTagId(UUID.fromString(tag)))
				.collect(Collectors.toList());
		final List<Movie> foundMovies = moviesService.getMoviesByTags(list, new UserId(userId));
		return ResponseEntity.status(200).body(foundMovies);
	}

	@Data
	private static class TagsIdsDto {

		private List<String> tags;

	}

}

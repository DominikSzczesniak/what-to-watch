package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class FindAllMoviesByTagIdController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies/tags/{tagId}")
	public ResponseEntity<?> findAllMoviesByTagId(@RequestHeader Integer userId, @PathVariable String tagId) {
		final List<MovieId> foundMovies = moviesService.getMoviesByTagId(new TagId(UUID.fromString(tagId)), new UserId(userId));
		return ResponseEntity.status(200).body(foundMovies);
	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
@RestController
public class RemoveMovieFromListController {

	private final MoviesService moviesService;

	@DeleteMapping("/api/movies/{movieId}")
	public ResponseEntity<?> removeMovieFromList(@RequestHeader("userId") Integer userId, @PathVariable Integer movieId) {
		moviesService.removeMovieFromList(new MovieId(movieId), new UserId(userId));
		return ResponseEntity.noContent().build();
	}

}

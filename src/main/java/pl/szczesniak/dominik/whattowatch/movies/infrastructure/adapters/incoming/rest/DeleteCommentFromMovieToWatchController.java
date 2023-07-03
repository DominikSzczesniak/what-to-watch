package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DeleteCommentFromMovieToWatchController {

	private final MoviesService moviesService;

	@DeleteMapping("/api/movies/{movieId}/comments")
	public ResponseEntity<?> deleteCommentFromMovieToWatch(@RequestHeader("userId") final Integer userId,
														   @PathVariable final Integer movieId,
														   @RequestBody final String commentId) {
		moviesService.deleteCommentFromMovie(new DeleteCommentFromMovie(
				new UserId(userId),
				new MovieId(movieId),
				UUID.fromString(commentId.trim()))
		);
		return ResponseEntity.status(204).build();
	}

}

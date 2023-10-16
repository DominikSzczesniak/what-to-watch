package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DeleteCommentFromMovieToWatchController {

	private final MoviesFacade moviesFacade;

	@DeleteMapping("/api/movies/{movieId}/comments/{commentId}")
	public ResponseEntity<?> deleteCommentFromMovieToWatch(@RequestHeader("userId") final Integer userId,
														   @PathVariable final Integer movieId,
														   @PathVariable final String commentId) {
		moviesFacade.deleteCommentFromMovie(new DeleteCommentFromMovie(
				new UserId(userId),
				new MovieId(movieId),
				new CommentId(UUID.fromString(commentId)))
		);
		return ResponseEntity.status(204).build();
	}

}

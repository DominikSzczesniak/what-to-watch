package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DeleteCommentFromMovieToWatchController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@DeleteMapping("/api/movies/{movieId}/comments/{commentId}")
	public ResponseEntity<?> deleteCommentFromMovieToWatch(@AuthenticationPrincipal final UserDetails userDetails,
														   @PathVariable final Integer movieId,
														   @PathVariable final String commentId) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		moviesFacade.deleteCommentFromMovie(new DeleteCommentFromMovie(
				userId,
				new MovieId(movieId),
				new CommentId(UUID.fromString(commentId)))
		);
		return ResponseEntity.status(204).build();
	}

}

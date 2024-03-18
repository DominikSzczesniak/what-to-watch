package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovie;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AddCommentToMovieController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@PostMapping("/api/movies/{movieId}/comments")
	public ResponseEntity<?> addCommentToMovie(@AuthenticationPrincipal final UserDetails userDetails,
											   @PathVariable final Integer movieId,
											   @RequestBody final CommentDto commentDto) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final UUID id = moviesFacade.addCommentToMovie(new AddCommentToMovie(userId, new MovieId(movieId), commentDto.getComment()));
		return ResponseEntity.status(201).body(id.toString());
	}

	@Data
	private static class CommentDto {

		private String comment;

	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AddCommentToMovieController {

	private final MoviesFacade moviesFacade;

	@PostMapping("/api/movies/{movieId}/comments")
	public ResponseEntity<?> addCommentToMovie(@RequestHeader("userId") final Integer userId,
											   @PathVariable final Integer movieId,
											   @RequestBody final CommentDto commentDto) {
		final UUID id = moviesFacade.addCommentToMovie(new AddCommentToMovie(new UserId(userId), new MovieId(movieId), commentDto.getComment()));
		return ResponseEntity.status(201).body(id.toString());
	}

	@Data
	private static class CommentDto {

		private String comment;

	}

}

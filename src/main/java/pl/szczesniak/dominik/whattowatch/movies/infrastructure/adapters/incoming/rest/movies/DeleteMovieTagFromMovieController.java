package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DeleteMovieTagFromMovieController {

	private final MoviesFacade moviesFacade;

	@DeleteMapping("/api/movies/{movieId}/tags/{tagId}")
	public ResponseEntity<?> deleteMovieTagFromMovie(@RequestHeader("userId") final Integer userId,
													 @PathVariable final Integer movieId,
													 @PathVariable final String tagId) {
		moviesFacade.deleteTagFromMovie(new DeleteTagFromMovie(
				new UserId(userId),
				new MovieId(movieId),
				new MovieTagId(UUID.fromString(tagId))
		));
		return ResponseEntity.status(204).build();
	}

}

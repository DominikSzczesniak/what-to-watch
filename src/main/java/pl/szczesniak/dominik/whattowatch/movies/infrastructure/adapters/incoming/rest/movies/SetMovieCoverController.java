package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
@RestController
public class SetMovieCoverController {

	private final MoviesFacade moviesFacade;

	@PutMapping("/api/movies/{movieId}/cover")
	public ResponseEntity<?> setMovieCover(@RequestHeader("userId") final Integer userId, @PathVariable final Integer movieId,
										   @RequestParam("image") final MultipartFile image) {
		final Try<SetMovieCover> setMovieCoverTry = Try.of(() -> new SetMovieCover(
				new UserId(userId),
				new MovieId(movieId),
				image.getOriginalFilename(),
				image.getContentType(),
				image.getInputStream()
		));

		return setMovieCoverTry.map(movieCover -> {
					moviesFacade.setMovieCover(movieCover);
					return ResponseEntity.status(HttpStatus.OK).build();
				})
				.getOrElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}

}

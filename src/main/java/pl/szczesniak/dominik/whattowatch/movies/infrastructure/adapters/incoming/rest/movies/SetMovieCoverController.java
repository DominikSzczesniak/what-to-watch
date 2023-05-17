package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class SetMovieCoverController {

	private final MoviesService moviesService;

	@PutMapping("/api/movies/{movieId}/cover")
	public ResponseEntity<?> setMovieCover(@RequestHeader("userId") Integer userId, @PathVariable Integer movieId,
										   @RequestParam("image") MultipartFile image) throws IOException {
		SetMovieCover command;
		try {
			command = new SetMovieCover(
					new UserId(userId),
					new MovieId(movieId),
					image.getOriginalFilename(),
					image.getContentType(),
					image.getInputStream());
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		moviesService.setMovieCover(command);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}

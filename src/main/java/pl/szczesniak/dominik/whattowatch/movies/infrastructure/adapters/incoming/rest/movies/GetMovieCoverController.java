package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.InputStream;

@RequiredArgsConstructor
@RestController
public class GetMovieCoverController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies/{movieId}/cover")
	public ResponseEntity<?> getMovieCover(@RequestHeader("userId") final Integer userId, @PathVariable final Integer movieId) {
		final MovieCoverDTO movieCover = moviesService.getCoverForMovie(new MovieId(movieId), new UserId(userId));
		final InputStream imageData = movieCover.getCoverContent();

		final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
				.filename(movieCover.getFilename())
				.build();

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(contentDisposition);

		return ResponseEntity
				.status(HttpStatus.OK)
				.headers(headers)
				.contentType(MediaType.valueOf(movieCover.getCoverContentType()))
				.body(new InputStreamResource(imageData));
	}

}
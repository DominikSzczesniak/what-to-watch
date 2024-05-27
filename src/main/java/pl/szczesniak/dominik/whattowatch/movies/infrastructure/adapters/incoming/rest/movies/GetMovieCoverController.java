package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.io.InputStream;

@RequiredArgsConstructor
@RestController
public class GetMovieCoverController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@GetMapping("/api/movies/{movieId}/cover")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<?> getMovieCover(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable final Integer movieId) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final MovieCoverDTO movieCover = moviesFacade.getCoverForMovie(new MovieId(movieId), userId);
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
package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

@RequiredArgsConstructor
@RestController
public class SetMovieCoverController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@PutMapping("/api/movies/{movieId}/cover")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<?> setMovieCover(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable final Integer movieId,
										   @RequestParam("image") final MultipartFile image) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final Try<SetMovieCover> setMovieCoverTry = Try.of(() -> new SetMovieCover(
				userId,
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

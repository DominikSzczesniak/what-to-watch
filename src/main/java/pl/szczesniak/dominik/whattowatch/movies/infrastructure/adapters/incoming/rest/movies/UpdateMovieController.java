package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

@RequiredArgsConstructor
@RestController
public class UpdateMovieController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@PutMapping("/api/movies/{movieId}")
	@PreAuthorize("hasAnyRole('USER')")
	public void updateMovie(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable final Integer movieId,
							@RequestBody final UpdateMovieDto updateMovieDto) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		moviesFacade.updateMovie(UpdateMovie.builder(new MovieId(movieId), userId, new MovieTitle(updateMovieDto.getMovieTitle())).build());
	}

	@Data
	private static class UpdateMovieDto {
		private String movieTitle;
	}

}

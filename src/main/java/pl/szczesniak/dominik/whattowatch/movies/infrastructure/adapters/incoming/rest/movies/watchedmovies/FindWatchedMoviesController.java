package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.query.model.GetWatchedMoviesToWatch;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedWatchedMovies;
import pl.szczesniak.dominik.whattowatch.movies.query.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FindWatchedMoviesController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@GetMapping("/api/movies/watched")
	@PreAuthorize("hasAnyRole('USER')")
	public PagedWatchedMoviesDto findWatchedMovies(@AuthenticationPrincipal final UserDetails userDetails,
												   @RequestParam(value = "page") final Integer page,
												   @RequestParam(value = "moviesPerPage") final Integer moviesPerPage) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final PagedWatchedMovies pagedWatchedMovies = moviesFacade.getWatchedMovies(
				new GetWatchedMoviesToWatch(userId, page, moviesPerPage));

		return new PagedWatchedMoviesDto(
				pagedWatchedMovies.getMovies(), pagedWatchedMovies.getPage(), pagedWatchedMovies.getTotalPages(), pagedWatchedMovies.getTotalMovies());
	}

	@Value
	public static class PagedWatchedMoviesDto {
		@NonNull List<WatchedMovieQueryResult> movies;

		@NonNull Integer page;

		@NonNull Integer totalPages;

		@NonNull Integer totalMovies;
	}

}

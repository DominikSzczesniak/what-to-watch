package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.query.model.GetMoviesByTags;
import pl.szczesniak.dominik.whattowatch.movies.query.model.GetMoviesToWatch;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedMovies;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindAllMoviesToWatchController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@GetMapping("/api/movies")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<PagedMoviesDto> findAllMoviesToWatch(@AuthenticationPrincipal final UserDetails userDetails,
															   @RequestParam(value = "tags", required = false) final String tags,
															   @RequestParam(value = "page") final Integer page,
															   @RequestParam(value = "moviesPerPage") final Integer moviesPerPage) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final PagedMovies pagedMovies = getPagedMovies(userId, tags, page, moviesPerPage);
		final PagedMoviesDto pagedMoviesDto = new PagedMoviesDto(
				pagedMovies.getMovies(), pagedMovies.getPage(), pagedMovies.getTotalPages(), pagedMovies.getTotalMovies());
		return ResponseEntity.status(200).body(pagedMoviesDto);
	}

	private PagedMovies getPagedMovies(final UserId userId, final String tags, final Integer page, final Integer moviesPerPage) {
		final PagedMovies foundMovies;
		if (tags == null) {
			foundMovies = moviesFacade.getMoviesToWatch(new GetMoviesToWatch(userId, page, moviesPerPage));
		} else {
			final String[] tagValues = tags.split(",");
			final List<MovieTagId> tagIds = Arrays.stream(tagValues)
					.map(tag -> new MovieTagId(UUID.fromString(tag)))
					.collect(Collectors.toList());
			foundMovies = moviesFacade.getMoviesByTags(new GetMoviesByTags(tagIds, userId, page, moviesPerPage));
		}
		return foundMovies;
	}

	@Value
	public static class PagedMoviesDto {
		@NonNull List<MovieInListQueryResult> movies;
		@NonNull Integer page;
		@NonNull Integer totalPages;
		@NonNull Integer totalMovies;
	}

}

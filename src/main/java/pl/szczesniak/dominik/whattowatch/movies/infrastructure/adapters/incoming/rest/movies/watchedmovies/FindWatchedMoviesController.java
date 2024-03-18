package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.query.model.GetWatchedMoviesToWatch;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedWatchedMovies;
import pl.szczesniak.dominik.whattowatch.movies.query.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FindWatchedMoviesController {

	private final MoviesFacade moviesFacade;

	@GetMapping("/api/movies/watched")
	public PagedWatchedMoviesDto findWatchedMovies(@RequestHeader("userId") Integer userId,
												   @RequestParam(value = "page") final Integer page,
												   @RequestParam(value = "moviesPerPage") final Integer moviesPerPage) {
		final PagedWatchedMovies pagedWatchedMovies = moviesFacade.getWatchedMovies(
				new GetWatchedMoviesToWatch(new UserId(userId), page, moviesPerPage));

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

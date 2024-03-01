package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.GetWatchedMoviesToWatch;
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
		final List<WatchedMovieDto> movies = toDto(pagedWatchedMovies.getMovies());

		final PagedWatchedMoviesDto pagedWatchedMoviesDto = new PagedWatchedMoviesDto(
				movies, pagedWatchedMovies.getPage(), pagedWatchedMovies.getTotalPages(), pagedWatchedMovies.getTotalMovies());

		return pagedWatchedMoviesDto;
	}

	private List<WatchedMovieDto> toDto(final List<WatchedMovieQueryResult> movies) {
		return movies.stream()
				.map(movie -> new WatchedMovieDto(movie.getTitle(), movie.getMovieId(), movie.getUserId())).toList();
	}

	@Value
	public static class PagedWatchedMoviesDto {
		@NonNull List<WatchedMovieDto> movies;

		@NonNull Integer page;

		@NonNull Integer totalPages;

		@NonNull Integer totalMovies;
	}

	@Value
	private static class WatchedMovieDto {
		String title;
		Integer movieId;
		Integer userId;
	}

}

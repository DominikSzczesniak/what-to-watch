package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.GetMoviesByTags;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.GetMoviesToWatch;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedMovies;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindAllMoviesToWatchController {

	private final MoviesFacade moviesFacade;

	@GetMapping("/api/movies")
	public ResponseEntity<PagedMoviesDto> findAllMoviesToWatch(@RequestHeader("userId") final Integer userId,
															   @RequestParam(value = "tags", required = false) final String tags,
															   @RequestParam(value = "page") final Integer page,
															   @RequestParam(value = "moviesPerPage") final Integer moviesPerPage) {
		final PagedMovies pagedMovies = getPagedMovies(userId, tags, page, moviesPerPage);
		final List<MovieDetailsDto> movies = createMovieDetailsDto(userId, pagedMovies.getMovies());
		final PagedMoviesDto pagedMoviesDto = new PagedMoviesDto(movies, pagedMovies.getPage(), pagedMovies.getTotalPages(), pagedMovies.getTotalMovies());
		return ResponseEntity.status(200).body(pagedMoviesDto);
	}

	private PagedMovies getPagedMovies(final Integer userId, final String tags, final Integer page, final Integer moviesPerPage) {
		final PagedMovies foundMovies;
		if (tags == null) {
			foundMovies = moviesFacade.getMoviesToWatch(new GetMoviesToWatch(new UserId(userId), page, moviesPerPage));
		} else {
			final String[] tagValues = tags.split(",");
			final List<MovieTagId> tagIds = Arrays.stream(tagValues)
					.map(tag -> new MovieTagId(UUID.fromString(tag)))
					.collect(Collectors.toList());
			foundMovies = moviesFacade.getMoviesByTags(new GetMoviesByTags(tagIds, new UserId(userId), page, moviesPerPage));
		}
		return foundMovies;
	}

	private static List<MovieDetailsDto> createMovieDetailsDto(final Integer userId, final List<MovieInListQueryResult> foundMovies) {
		return foundMovies.stream().map(movie ->
				new MovieDetailsDto(
						movie.getTitle(),
						movie.getMovieId(),
						userId
				)).toList();
	}

	@Value
	public static class PagedMoviesDto {
		@NonNull List<MovieDetailsDto> movies;

		@NonNull Integer page;

		@NonNull Integer totalPages;

		@NonNull Integer totalMovies;
	}


	@Value
	public static class MovieDetailsDto {
		String title;
		Integer movieId;
		Integer userId;
	}

	@Value
	public static class MovieTagDto {
		String tagId;
		String tagLabel;
	}

}

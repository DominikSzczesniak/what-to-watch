package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
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
	public ResponseEntity<List<MovieDetailsDto>> findAllMoviesToWatch(@RequestHeader("userId") final Integer userId,
																	  @RequestParam(value = "tags", required = false) final String tags) {
		final List<Movie> foundMovies = findMovies(userId, tags);
		final List<MovieDetailsDto> movies = createMovieDetailsDto(userId, foundMovies);
		return ResponseEntity.status(200).body(movies);
	}

	private List<Movie> findMovies(final Integer userId, final String tags) {
		final List<Movie> foundMovies;
		if (tags == null) {
			foundMovies = moviesFacade.getMoviesToWatch(new UserId(userId));
		} else {
			final String[] tagValues = tags.split(",");
			final List<MovieTagId> tagIds = Arrays.stream(tagValues)
					.map(tag -> new MovieTagId(UUID.fromString(tag)))
					.collect(Collectors.toList());
			foundMovies = moviesFacade.getMoviesByTags(tagIds, new UserId(userId));
		}
		return foundMovies;
	}

	private static List<MovieDetailsDto> createMovieDetailsDto(final Integer userId, final List<Movie> foundMovies) {
		return foundMovies.stream().map(movie ->
				new MovieDetailsDto(
						movie.getTitle().getValue(),
						movie.getMovieId().getValue(),
						userId,
						mapMovieComments(movie),
						mapMovieTags(movie)
				)).toList();
	}

	private static List<MovieTagDto> mapMovieTags(final Movie movie) {
		return movie.getTags().stream()
				.map(movieTag -> new MovieTagDto(movieTag.getTagId().getValue(), movieTag.getLabel().getValue()))
				.toList();
	}

	private static List<MovieCommentDto> mapMovieComments(final Movie movie) {
		return movie.getComments().stream()
				.map(movieComment -> new MovieCommentDto(
						movieComment.getCommentId().getValue().toString(),
						movieComment.getText()))
				.collect(Collectors.toList());
	}

	@Value
	public static class MovieDetailsDto {
		String title;
		Integer movieId;
		Integer userId;
		List<MovieCommentDto> comments;
		List<MovieTagDto> tags;
	}

	@Value
	public static class MovieCommentDto {
		String commentId;
		String value;
	}

	@Value
	public static class MovieTagDto {
		String tagId;
		String tagLabel;
	}

}

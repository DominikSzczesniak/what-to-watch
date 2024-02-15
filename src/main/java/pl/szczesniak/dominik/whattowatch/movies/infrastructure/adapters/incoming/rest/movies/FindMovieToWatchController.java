package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindMovieToWatchController {

	private final MoviesFacade moviesFacade;

	@GetMapping("/api/movies/{movieId}")
	public MovieDetailsDto findMovieToWatch(@RequestHeader("userId") final Integer userId, @PathVariable Integer movieId) {
		final MovieQueryResult movie = moviesFacade.getMovie(new MovieId(movieId), new UserId(userId));
		return new MovieDetailsDto(
				movie.getTitle().getValue(),
				movieId,
				userId,
				mapMovieComments(movie),
				mapMovieTags(movie)
		);
	}

	private static List<MovieTagDto> mapMovieTags(final MovieQueryResult movie) {
		return movie.getTags().stream()
				.map(movieTag -> new MovieTagDto(movieTag.getTagId().getValue().toString(), movieTag.getLabel().getValue()))
				.toList();
	}

	private static List<MovieCommentDto> mapMovieComments(final MovieQueryResult movie) {
		return movie.getComments().stream()
				.map(movieComment -> new MovieCommentDto(
						movieComment.getCommentId().getValue().toString(),
						movieComment.getText()))
				.collect(Collectors.toList());
	}

	@Value
	private static class MovieDetailsDto {
		String title;
		Integer movieId;
		Integer userId;
		List<MovieCommentDto> comments;
		List<MovieTagDto> tags;
	}

	@Value
	private static class MovieCommentDto {
		String commentId;
		String value;
	}

	@Value
	private static class MovieTagDto {
		String tagId;
		String tagLabel;
	}

}

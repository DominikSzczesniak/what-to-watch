package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindMovieToWatchController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@GetMapping("/api/movies/{movieId}")
	public MovieDetailsDto findMovieToWatch(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable Integer movieId) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final MovieQueryResult movie = moviesFacade.getMovie(new MovieId(movieId), userId);
		return new MovieDetailsDto(
				movie.getTitle(),
				movieId,
				userId.getValue(),
				mapMovieComments(movie),
				mapMovieTags(movie)
		);
	}

	private static List<MovieTagDto> mapMovieTags(final MovieQueryResult movie) {
		return movie.getTags().stream()
				.map(movieTag -> new MovieTagDto(movieTag.getTagId(), movieTag.getLabel()))
				.toList();
	}

	private static List<MovieCommentDto> mapMovieComments(final MovieQueryResult movie) {
		return movie.getComments().stream()
				.map(movieComment -> new MovieCommentDto(
						movieComment.getCommentId(),
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

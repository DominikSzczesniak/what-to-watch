package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindMovieTagsController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@GetMapping("/api/movies/tags")
	public List<MovieTagDto> findTagByUserId(@AuthenticationPrincipal final UserDetails userDetails) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final List<MovieTagQueryResult> movieTagsByUserId = moviesFacade.getMovieTagsByUserId(userId.getValue());
		return movieTagsByUserId.stream()
				.map(movieTag -> new MovieTagDto(
						movieTag.getTagId(),
						movieTag.getLabel(),
						movieTag.getUserId()))
				.collect(Collectors.toList());
	}

	@Value
	private static class MovieTagDto {
		String tagId;
		String tagLabel;
		Integer userId;
	}

}

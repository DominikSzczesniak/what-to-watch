package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovie;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AddMovieTagToMovieController {

	private final MoviesFacade moviesFacade;
	private final LoggedInUserProvider loggedInUserProvider;

	@PostMapping("/api/movies/{movieId}/tags")
	public ResponseEntity<?> addMovieTagToMovie(@AuthenticationPrincipal final UserDetails userDetails,
												@RequestParam(value = "tagId", required = false) final Optional<String> tagId,
												@PathVariable final Integer movieId,
												@RequestBody(required = false) final MovieTagDto movieTagDto) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final MovieTagId id = new MovieTagId(UUID.fromString(tagId.orElse(String.valueOf(UUID.randomUUID()))));
		final Optional<MovieTagQueryResult> foundTag = moviesFacade.getTagByTagId(id);
		final MovieTagLabel label = getTagLabel(userId.getValue(), movieTagDto, foundTag);

		final AddTagToMovie addTagToMovie = new AddTagToMovie(
				userId,
				new MovieId(movieId),
				label,
				id);

		final MovieTagId createdTagId = moviesFacade.addTagToMovie(addTagToMovie);

		return ResponseEntity.status(201).body(createdTagId.getValue());
	}

	private static MovieTagLabel getTagLabel(final Integer userId, final MovieTagDto movieTagDto, final Optional<MovieTagQueryResult> foundTag) {
		return foundTag.filter(tag -> tag.getUserId().equals(userId))
				.map(movieTagQueryResult -> new MovieTagLabel(movieTagQueryResult.getLabel()))
				.orElseGet(() -> {
					if (movieTagDto == null) {
						throw new ObjectDoesNotExistException("MovieTag does not belong to user");
					}
					return new MovieTagLabel(movieTagDto.getTagLabel().orElseThrow(() ->
							new ObjectDoesNotExistException("No allowed tagId or TagLabel provided")));
				});
	}

	@Data
	private static class MovieTagDto {
		private Optional<String> tagLabel;
	}

}

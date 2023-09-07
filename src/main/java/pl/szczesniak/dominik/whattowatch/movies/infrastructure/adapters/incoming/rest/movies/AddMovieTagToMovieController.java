package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.MovieTag;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AddMovieTagToMovieController {

	private final MoviesService moviesService;

	@PostMapping("/api/movies/{movieId}/tags")
	public ResponseEntity<?> addMovieTagToMovie(@RequestHeader("userId") final Integer userId,
												@RequestParam(value = "tagId", required = false) final Optional<String> tagId,
												@PathVariable final Integer movieId,
												@RequestBody(required = false) final MovieTagDto movieTagDto) {
		final TagId id = new TagId(UUID.fromString(tagId.orElse(String.valueOf(UUID.randomUUID()))));
		final Optional<MovieTag> foundTag = moviesService.getTagByTagId(id);
		final TagLabel label = getTagLabel(userId, movieTagDto, foundTag);

		final AddTagToMovie addTagToMovie = new AddTagToMovie(
				new UserId(userId),
				new MovieId(movieId),
				label,
				id);

		final TagId createdTagId = moviesService.addTagToMovie(addTagToMovie);

		return ResponseEntity.status(201).body(createdTagId.getValue().toString());
	}

	private static TagLabel getTagLabel(final Integer userId, final MovieTagDto movieTagDto, final Optional<MovieTag> foundTag) {
		return foundTag.filter(tag -> tag.getUserId().getValue().equals(userId))
				.map(MovieTag::getLabel)
				.orElseGet(() -> {
					if (movieTagDto == null) {
						throw new ObjectDoesNotExistException("MovieTag does not belong to user");
					}
					return new TagLabel(movieTagDto.getTagLabel().orElseThrow(() ->
							new ObjectDoesNotExistException("No allowed tagId or TagLabel provided")));
				});
	}

	@Data
	private static class MovieTagDto {
		private Optional<String> tagLabel;
	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MovieTag;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FindMovieTagsController {

	private final MoviesFacade moviesFacade;

	@GetMapping("/api/movies/tags")
	public List<MovieTagDto> findTagByUserId(@RequestHeader final Integer userId) {
		List<MovieTag> movieTagsByUserId = moviesFacade.getMovieTagsByUserId(userId);
		return movieTagsByUserId.stream()
				.map(movieTag -> new MovieTagDto(
						movieTag.getTagId().getValue(),
						movieTag.getLabel().getValue(),
						movieTag.getUserId().getValue()))
				.collect(Collectors.toList());
	}

	@Value
	private static class MovieTagDto {
		String tagId;
		String tagLabel;
		Integer userId;
	}

}

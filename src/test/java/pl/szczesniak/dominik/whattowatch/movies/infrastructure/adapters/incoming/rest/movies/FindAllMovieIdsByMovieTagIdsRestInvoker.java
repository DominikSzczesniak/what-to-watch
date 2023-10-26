package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllMovieIdsByMovieTagIdsRestInvoker {

	private static final String URL = "/api/movies/tags/filter";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<MovieDetailsByTagDto>> getMoviesByTagIds(final Integer userId, final TagIdsDto tags) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		final HttpEntity<TagIdsDto> requestEntity = new HttpEntity<>(tags, headers);

		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<>() {}
		);
	}

	@Data
	public static class TagIdsDto {

		private final List<String> tags;

	}

	@Value
	public static class MovieDetailsByTagDto {
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

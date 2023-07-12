package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddCommentToMovieRestInvoker {

	private static final String URL = "/api/movies/{movieId}/comments";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<String> addCommentToMovie(final Integer userId, final Integer movieId, final CommentDto commentDto) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		final HttpEntity<CommentDto> requestEntity = new HttpEntity<>(commentDto, headers);
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				requestEntity,
				String.class,
				movieId
		);
	}

	@Data
	@Builder
	public static class CommentDto {

		private String comment;

	}

}

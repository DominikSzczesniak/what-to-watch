package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

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
public class DeleteCommentFromMovieToWatchRestInvoker {

	private static final String URL = "/api/movies/{movieId}/comments/{commentId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<Void> deleteCommentFromMovieToWatch(final Integer userId,
															  final Integer movieId,
															  final DeleteCommentDto deleteCommentDto) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.DELETE,
				new HttpEntity<>(headers),
				void.class,
				movieId,
				deleteCommentDto.getCommentId()
		);
	}

	@Data
	public static class DeleteCommentDto {

		private final String commentId;

	}

}

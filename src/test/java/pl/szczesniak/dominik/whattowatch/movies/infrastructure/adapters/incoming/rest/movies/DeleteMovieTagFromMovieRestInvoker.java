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
public class DeleteMovieTagFromMovieRestInvoker {

	private static final String URL = "/api/movies/{movieId}/tags/{tagId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<Void> deleteTagFromMovie(final String tagId, final Integer userId, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		final HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		return restTemplate.exchange(
				URL,
				HttpMethod.DELETE,
				requestEntity,
				Void.class,
				movieId,
				tagId
		);
	}

}

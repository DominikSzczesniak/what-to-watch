package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.invokers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMovieToWatchCoverRestInvoker {

	private static final String URL = "/api/movies/{movieId}/cover";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<byte[]> getMovieToWatchCover(final Integer userId, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				byte[].class,
				movieId
		);
	}

}

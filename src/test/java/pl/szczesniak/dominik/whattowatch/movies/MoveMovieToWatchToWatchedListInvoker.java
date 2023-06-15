package pl.szczesniak.dominik.whattowatch.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoveMovieToWatchToWatchedListInvoker {

	private static final String URL = "/api/movies/{movieId}/watched";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<Void> findMoviesToWatch(final Integer userId, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(headers),
				void.class,
				movieId
				);
	}

}

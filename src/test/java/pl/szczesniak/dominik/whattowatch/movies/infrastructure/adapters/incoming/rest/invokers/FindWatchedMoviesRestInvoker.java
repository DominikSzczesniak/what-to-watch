package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.invokers;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
public class FindWatchedMoviesRestInvoker {

	private static final String URL = "/api/movies/watched";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<WatchedMovieDto>> findWatchedMovies(final Integer userId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {
				}
		);
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	@Builder
	public static class WatchedMovieDto {

		private final String title;
		private final Integer movieId;
		private final Integer userId;

	}

}

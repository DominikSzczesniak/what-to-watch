package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

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
public class FindWatchedMoviesRestInvoker {

	private static final String URL = "/api/movies/watched";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<WatchedMovieDto>> findWatchedMovies(final HttpHeaders headers) {
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {}
		);
	}

	@Value
	public static class WatchedMovieDto {
		String title;
		Integer movieId;
		Integer userId;
	}

}

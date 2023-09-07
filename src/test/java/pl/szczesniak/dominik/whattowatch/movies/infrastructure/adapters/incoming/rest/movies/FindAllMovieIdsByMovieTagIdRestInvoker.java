package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllMovieIdsByMovieTagIdRestInvoker {

	private static final String URL = "/api/movies/tags/{tagId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<MovieId>> getMoviesByTagId(final Integer userId, final String tagId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {},
				tagId
		);
	}

}

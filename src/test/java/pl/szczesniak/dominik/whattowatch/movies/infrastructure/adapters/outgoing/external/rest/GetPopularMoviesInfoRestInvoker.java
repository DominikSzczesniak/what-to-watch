package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfo;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPopularMoviesInfoRestInvoker {

	private static final String URL = "/api/movies/info";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<MovieInfo>> getPopularMovies() {
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {				}
		);
	}

}

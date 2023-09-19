package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfo;

@Component
@RequiredArgsConstructor
public class FindMovieInfoByIdRestInvoker {

	private static final String URL = "/api/movies/info/{movieId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<MovieInfo> getMovieInfoGenres(final Long movieId) {
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {},
				movieId
		);
	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.Genre;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.GenresResponse;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMovieInfoGenresRestInvoker {

	private static final String URL = "/api/genres";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<Genre>> getMovieInfoGenres() {
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {}
		);
	}

}

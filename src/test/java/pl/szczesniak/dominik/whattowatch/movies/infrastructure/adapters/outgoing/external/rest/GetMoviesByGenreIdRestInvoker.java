package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.MovieInfo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMoviesByGenreIdRestInvoker {

	private static final String URL = "/api/movies/info/genre/{genreId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<List<MovieInfo>> getMoviesByGenreId(final Long genreId) {
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<MovieInfo>>() {
				},
				genreId
		);
	}

}

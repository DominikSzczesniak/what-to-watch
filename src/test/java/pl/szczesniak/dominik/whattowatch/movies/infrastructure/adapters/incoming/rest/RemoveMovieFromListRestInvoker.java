package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

import lombok.AllArgsConstructor;
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
public class RemoveMovieFromListRestInvoker {

	private static final String URL = "/api/movies/{movieId}";

	private final TestRestTemplate restTemplate;

	public <T> ResponseEntity<T> removeMovie(final HttpHeaders headers, final Class<T> responseType, final Integer movieId) {
		return restTemplate.exchange(
				URL,
				HttpMethod.DELETE,
				new HttpEntity<>(headers),
				responseType,
				movieId
		);
	}

	@Data
	@AllArgsConstructor
	public static class CreateMovieDto {
		private String title;
		private Integer userId;
	}

}

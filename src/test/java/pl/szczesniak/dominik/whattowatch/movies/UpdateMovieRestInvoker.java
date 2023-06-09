package pl.szczesniak.dominik.whattowatch.movies;

import lombok.Builder;
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
public class UpdateMovieRestInvoker {

	private static final String URL = "/api/movies/{movieId}";

	private final TestRestTemplate restTemplate;

	public <T> ResponseEntity<T> updateMovie(final HttpHeaders headers, final UpdateMovieDto updateMovieDto, final Class<T> responseType, final Integer movieId) {
		final HttpEntity<UpdateMovieDto> requestEntity = new HttpEntity<>(updateMovieDto, headers);
		return restTemplate.exchange(
				URL,
				HttpMethod.PUT,
				requestEntity,
				responseType,
				movieId
		);
	}

	@Data
	@Builder
	public static class UpdateMovieDto {

		private final String movieTitle;

	}

}

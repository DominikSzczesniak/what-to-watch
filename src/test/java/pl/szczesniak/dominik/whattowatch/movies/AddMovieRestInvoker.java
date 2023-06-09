package pl.szczesniak.dominik.whattowatch.movies;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddMovieRestInvoker {

	private static final String URL = "/api/movies";

	private final TestRestTemplate restTemplate;

	public <T> ResponseEntity<T> addMovie(final CreateMovieDto createMovieDto, final Class<T> responseType) {
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(createMovieDto),
				responseType
		);
	}

	@Data
	@Builder
	public static class CreateMovieDto {

		private String title;
		private Integer userId;

	}

}

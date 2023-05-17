package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.invokers;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;

@Component
@RequiredArgsConstructor
public class AddMovieToWatchRestInvoker {

	private static final String URL = "/api/movies";

	private final TestRestTemplate restTemplate;

	public <T> ResponseEntity<T> addMovie(final AddMovieDto addMovieDto, final Class<T> responseType) {
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(addMovieDto),
				responseType
		);
	}

	@Data
	@Builder
	public static class AddMovieDto {

		@Builder.Default
		private String title = createAnyMovieTitle().getValue();
		private Integer userId;

	}

}

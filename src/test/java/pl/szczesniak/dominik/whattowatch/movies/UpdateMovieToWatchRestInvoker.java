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

import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;

@Component
@RequiredArgsConstructor
public class UpdateMovieToWatchRestInvoker {

	private static final String URL = "/api/movies/{movieId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<Void> updateMovie(final UpdateMovieDto updateMovieDto, final Integer userId, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		final HttpEntity<UpdateMovieDto> requestEntity = new HttpEntity<>(updateMovieDto, headers);
		return restTemplate.exchange(
				URL,
				HttpMethod.PUT,
				requestEntity,
				void.class,
				movieId
		);
	}

	@Data
	@Builder
	public static class UpdateMovieDto {

		@Builder.Default
		private final String movieTitle = createAnyMovieTitle().getValue();


	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;

@Component
public class UpdateMovieToWatchRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}";

	UpdateMovieToWatchRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<Void> updateMovie(final UpdateMovieDto updateMovieDto, final LoggedUser loggedUser, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
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

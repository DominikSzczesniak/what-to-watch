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
public class AddMovieToWatchRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies";

	AddMovieToWatchRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public <T> ResponseEntity<T> addMovie(final AddMovieDto addMovieDto, final LoggedUser loggedUser, final Class<T> responseType) {
		final HttpHeaders headers = new HttpHeaders();
		headers.put(HttpHeaders.COOKIE, loggedUser.getSessionId());

		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(addMovieDto, headers),
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

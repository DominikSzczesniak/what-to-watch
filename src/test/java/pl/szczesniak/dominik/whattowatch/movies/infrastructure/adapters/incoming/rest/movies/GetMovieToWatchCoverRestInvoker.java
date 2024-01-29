package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

@Component
public class GetMovieToWatchCoverRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}/cover";

	GetMovieToWatchCoverRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<byte[]> getMovieToWatchCover(final LoggedUser loggedUser, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				byte[].class,
				movieId
		);
	}

}

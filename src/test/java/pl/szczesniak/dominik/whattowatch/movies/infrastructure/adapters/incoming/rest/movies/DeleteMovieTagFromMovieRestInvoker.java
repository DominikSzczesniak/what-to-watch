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
public class DeleteMovieTagFromMovieRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}/tags/{tagId}";

	DeleteMovieTagFromMovieRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<Void> deleteTagFromMovie(final String tagId, final LoggedUser loggedUser, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		final HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		return restTemplate.exchange(
				URL,
				HttpMethod.DELETE,
				requestEntity,
				Void.class,
				movieId,
				tagId
		);
	}

}

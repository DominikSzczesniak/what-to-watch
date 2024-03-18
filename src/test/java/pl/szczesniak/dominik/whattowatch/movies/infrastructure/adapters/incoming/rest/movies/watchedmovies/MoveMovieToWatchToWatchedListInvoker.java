package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

@Component
public class MoveMovieToWatchToWatchedListInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}/watched";

	MoveMovieToWatchToWatchedListInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<Void> moveMovieToWatchedMovies(final LoggedUser loggedUser, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(headers),
				void.class,
				movieId
		);
	}

}

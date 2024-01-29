package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations;

import lombok.Data;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import java.util.List;

@Component
public class GetMovieGenresInvoker extends BaseRestInvoker {

	private static final String URL = "/api/genres";

	GetMovieGenresInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<MovieGenresDto> getMovieGenres(final LoggedUser loggedUser) {
		final HttpHeaders headers = new HttpHeaders();
		headers.put(HttpHeaders.COOKIE, loggedUser.getSessionId());
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				MovieGenresDto.class
		);
	}

	@Data
	public static class MovieGenresDto {

		List<String> genresNames;

	}

}

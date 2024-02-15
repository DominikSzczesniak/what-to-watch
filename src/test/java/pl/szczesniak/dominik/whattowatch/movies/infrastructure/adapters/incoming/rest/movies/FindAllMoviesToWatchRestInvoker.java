package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import java.util.List;

@Component
public class FindAllMoviesToWatchRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies";

	FindAllMoviesToWatchRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<List<MovieDetailsDto>> findAllMoviesToWatch(final LoggedUser loggedUser) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {
				}
		);
	}

	public ResponseEntity<List<MovieDetailsDto>> findAllMoviesToWatch(final LoggedUser loggedUser, final String tags) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		final String urlWithParams = URL + "?tags=" + tags;
		return restTemplate.exchange(
				urlWithParams,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {
				}
		);
	}

	@Value
	public static class MovieDetailsDto {
		String title;
		Integer movieId;
		Integer userId;
//		List<MovieCommentDto> comments;
		List<MovieTagDto> tags;
	}

	@Value
	public static class MovieCommentDto {
		String commentId;
		String value;
	}

	@Value
	public static class MovieTagDto {
		String tagId;
		String tagLabel;
	}

}

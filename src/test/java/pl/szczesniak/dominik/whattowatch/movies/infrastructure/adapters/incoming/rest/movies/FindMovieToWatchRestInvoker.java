package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Value;
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
public class FindMovieToWatchRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}";

	FindMovieToWatchRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<MovieDetailsDTO> findMovieToWatch(final LoggedUser loggedUser, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				MovieDetailsDTO.class,
				movieId
		);
	}

	@Value
	public static class MovieDetailsDTO {
		String title;
		Integer movieId;
		Integer userId;
		List<MovieCommentDto> comments;
		List<MovieTagDTO> tags;
	}

	@Value
	public static class MovieCommentDto {
		String commentId;
		Integer movieId;
		String value;
	}

	@Value
	public static class MovieTagDTO {
		String tagId;
		String tagLabel;
	}

}

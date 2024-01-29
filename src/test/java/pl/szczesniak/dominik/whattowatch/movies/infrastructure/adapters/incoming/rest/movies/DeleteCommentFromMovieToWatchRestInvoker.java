package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

@Component
public class DeleteCommentFromMovieToWatchRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}/comments/{commentId}";

	DeleteCommentFromMovieToWatchRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<Void> deleteCommentFromMovieToWatch(final LoggedUser loggedUser,
															  final Integer movieId,
															  final DeleteCommentDto deleteCommentDto) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		return restTemplate.exchange(
				URL,
				HttpMethod.DELETE,
				new HttpEntity<>(headers),
				void.class,
				movieId,
				deleteCommentDto.getCommentId()
		);
	}

	@Data
	public static class DeleteCommentDto {

		private final String commentId;

	}

}

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

@Component
public class AddCommentToMovieRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}/comments";

	AddCommentToMovieRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}


	public ResponseEntity<String> addCommentToMovie(final LoggedUser loggedUser, final Integer movieId, final CommentDto commentDto) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		final HttpEntity<CommentDto> requestEntity = new HttpEntity<>(commentDto, headers);
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				requestEntity,
				String.class,
				movieId
		);
	}

	@Data
	@Builder
	public static class CommentDto {

		private String comment;

	}

}

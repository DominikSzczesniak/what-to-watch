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

import java.util.Optional;

@Component
public class AddMovieTagToMovieRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/{movieId}/tags";

	AddMovieTagToMovieRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}


	public ResponseEntity<String> addTagToMovie(final MovieTagDto movieTagDto, final LoggedUser loggedUser, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		final HttpEntity<MovieTagDto> requestEntity = new HttpEntity<>(movieTagDto, headers);

		return getAddTagToMovieResponse(movieTagDto, movieId, requestEntity);
	}

	private ResponseEntity<String> getAddTagToMovieResponse(final MovieTagDto movieTagDto,
															final Integer movieId,
															final HttpEntity<MovieTagDto> requestEntity) {
		if (movieTagDto.getTagId().isPresent()) {
			return restTemplate.exchange(
					"/api/movies/{movieId}/tags?tagId={tagId}",
					HttpMethod.POST,
					requestEntity,
					String.class,
					movieId,
					movieTagDto.getTagId().get()
			);
		}

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
	public static class MovieTagDto {

		private String tagLabel;

		private String tagId;

		Optional<String> getTagId() {
			return Optional.ofNullable(tagId);
		}
	}

}

package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AddMovieTagToMovieRestInvoker {

	private static final String URL = "/api/movies/{movieId}/tags";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<String> addTagToMovie(final MovieTagDTO movieTagDTO, final Integer userId, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		if (movieTagDTO.getTagId() != null) {
			headers.set("tagId", movieTagDTO.getTagId());
		}
		final HttpEntity<MovieTagDTO> requestEntity = new HttpEntity<>(movieTagDTO, headers);
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
	public static class MovieTagDTO {

		private Optional<String> tagLabel;

		private String tagId;

	}

}

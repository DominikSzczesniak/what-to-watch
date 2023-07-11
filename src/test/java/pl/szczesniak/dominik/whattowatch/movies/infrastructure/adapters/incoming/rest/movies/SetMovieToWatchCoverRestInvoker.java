package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SetMovieToWatchCoverRestInvoker {

	private static final String URL = "/api/movies/{movieId}/cover";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<?> setCover(final Integer userId, final Integer movieId, MultipartFile image) throws IOException {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", userId.toString());

		final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("image", new ByteArrayResource(image.getBytes()) {
			@Override
			public String getFilename() {
				return image.getOriginalFilename();
			}
		});

		final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		return restTemplate.exchange(
				URL,
				HttpMethod.PUT,
				requestEntity,
				Object.class,
				movieId
		);
	}

}

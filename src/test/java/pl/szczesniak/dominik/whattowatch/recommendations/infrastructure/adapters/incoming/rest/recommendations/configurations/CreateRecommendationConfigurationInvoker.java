package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateRecommendationConfigurationInvoker {

	private static final String URL = "/api/recommendations/configuration";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<Long> createRecommendationConfiguration(final Integer userId, final CreateRecommendationConfigurationDto dto) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.POST,
				new HttpEntity<>(dto, headers),
				Long.class
		);
	}

	@Data
	@Builder
	public static class CreateRecommendationConfigurationDto {

		private final List<String> genres;

	}

}

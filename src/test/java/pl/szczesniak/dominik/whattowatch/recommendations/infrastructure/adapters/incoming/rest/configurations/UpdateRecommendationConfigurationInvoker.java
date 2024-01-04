package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations;

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

@RequiredArgsConstructor
@Component
public class UpdateRecommendationConfigurationInvoker {

	private static final String URL = "/api/users/recommendations/configuration";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<Void> updateRecommendationConfiguration(final Integer userId, final UpdateRecommendationConfigurationDto dto) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.PUT,
				new HttpEntity<>(dto, headers),
				Void.class
		);
	}

	@Data
	@Builder
	public static class UpdateRecommendationConfigurationDto {

		private List<String> limitToGenres;

	}

}

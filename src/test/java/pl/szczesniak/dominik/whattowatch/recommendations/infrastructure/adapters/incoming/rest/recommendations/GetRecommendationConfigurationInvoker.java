package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations;


import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRecommendationConfigurationInvoker {

	private static final String URL = "/api/recommendations/configuration";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<RecommendationConfigurationDto> getRecommendationConfiguration(final Integer userId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				RecommendationConfigurationDto.class
		);
	}

	@Value
	public static class RecommendationConfigurationDto {

		Long configurationId;
		List<String> genreNames;
		Integer userId;

	}

}

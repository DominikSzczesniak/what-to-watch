package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest;


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
public class GetRecommendationConfigurationInvoker extends BaseRestInvoker {

	private static final String URL = "/api/recommendations/configuration";

	GetRecommendationConfigurationInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<RecommendationConfigurationDto> getRecommendationConfiguration(final LoggedUser loggedUser) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
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

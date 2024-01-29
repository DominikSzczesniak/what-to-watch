package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations;

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

import java.util.List;

@Component
public class UpdateRecommendationConfigurationInvoker extends BaseRestInvoker {

	private static final String URL = "/api/users/recommendations/configuration";

	UpdateRecommendationConfigurationInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<Void> updateRecommendationConfiguration(final LoggedUser loggedUser, final UpdateRecommendationConfigurationDto dto) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
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

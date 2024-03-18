package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations;

import com.sun.istack.NotNull;
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
public class CreateRecommendationConfigurationInvoker extends BaseRestInvoker {

	private static final String URL = "/api/users/recommendations/configuration";

	CreateRecommendationConfigurationInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<Long> createRecommendationConfiguration(final LoggedUser loggedUser, final CreateRecommendationConfigurationDto dto) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
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

		@NotNull
		private final List<String> limitToGenres;

	}

}

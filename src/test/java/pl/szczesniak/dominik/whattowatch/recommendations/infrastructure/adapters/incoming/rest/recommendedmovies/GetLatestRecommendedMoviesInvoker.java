package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GetLatestRecommendedMoviesInvoker extends BaseRestInvoker {

	private static final String URL = "/api/recommendations/latest";

	GetLatestRecommendedMoviesInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<RecommendedMoviesDto> getLatestRecommendedMovies(final LoggedUser loggedUser) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				RecommendedMoviesDto.class
		);
	}

	@Data
	public static class RecommendedMoviesDto {

		List<MovieInfoDto> movieInfos;
		LocalDateTime creationDate;
		LocalDateTime endInterval;

	}

	@Value
	public static class MovieInfoDto {

		String title;
		String overview;
		List<String> genresNames;
		Integer externalId;
		String externalApi;

	}

}

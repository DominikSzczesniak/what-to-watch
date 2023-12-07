package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetLatestRecommendedMoviesInvoker {

	private static final String URL = "/api/recommendations";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<RecommendedMoviesDto> getLatestRecommendedMovies(final Integer userId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
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

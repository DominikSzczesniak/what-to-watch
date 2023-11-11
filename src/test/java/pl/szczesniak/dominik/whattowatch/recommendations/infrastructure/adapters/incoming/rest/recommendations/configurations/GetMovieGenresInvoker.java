package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMovieGenresInvoker {

	private static final String URL = "/api/genres";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<MovieGenresDto> getMovieGenres() {
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				MovieGenresDto.class
		);
	}

	@Data
	public static class MovieGenresDto {

		List<String> genresNames;

	}

}

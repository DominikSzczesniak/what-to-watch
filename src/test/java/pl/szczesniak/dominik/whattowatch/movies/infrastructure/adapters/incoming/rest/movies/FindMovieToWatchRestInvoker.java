package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieTagToMovieRestInvoker.MovieTagDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindMovieToWatchRestInvoker {

	private static final String URL = "/api/movies/{movieId}";

	private final TestRestTemplate restTemplate;

	public ResponseEntity<MovieDetailsDto> findMovieToWatch(final Integer userId, final Integer movieId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return restTemplate.exchange(
				URL,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				MovieDetailsDto.class,
				movieId
		);
	}

	@Value
	public static class MovieDetailsDto {
		String title;
		Integer movieId;
		Integer userId;
		List<MovieCommentDto> comments;
		List<MovieTagDto> tags;
	}

	@Value
	public static class MovieCommentDto {
		String commentId;
		Integer movieId;
		String value;
	}

	@Value
	public static class MovieTagDto {
		String tagId;
		String tagLabel;
	}

}

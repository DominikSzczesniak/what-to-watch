package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import java.util.List;

@Component
public class FindAllMoviesToWatchRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies";

	FindAllMoviesToWatchRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<PagedMoviesDto> findAllMoviesToWatch(final LoggedUser loggedUser, final PaginationRequestDto paginationRequestDto) {
		final UriComponentsBuilder builder = UriComponentsBuilder.fromPath(URL)
				.queryParam("page", paginationRequestDto.getPage())
				.queryParam("moviesPerPage", paginationRequestDto.getPageSize());
		return executeRequest(loggedUser, builder);
	}

	public ResponseEntity<PagedMoviesDto> findAllMoviesToWatch(final LoggedUser loggedUser,
															   final String tags,
															   final PaginationRequestDto paginationRequestDto) {
		final UriComponentsBuilder builder = UriComponentsBuilder.fromPath(URL)
				.queryParam("tags", tags)
				.queryParam("page", paginationRequestDto.getPage())
				.queryParam("moviesPerPage", paginationRequestDto.getPageSize());
		return executeRequest(loggedUser, builder);
	}

	private ResponseEntity<PagedMoviesDto> executeRequest(final LoggedUser loggedUser, final UriComponentsBuilder builder) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		final String url = builder.toUriString();
		return restTemplate.exchange(
				url,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {
				}
		);
	}

	@Value
	public static class PagedMoviesDto {
		@NonNull List<MovieDetailsDto> movies;
		@NonNull Integer page;
		@NonNull Integer totalPages;
		@NonNull Integer totalMovies;
	}

	@Value
	public static class MovieDetailsDto {
		String title;
		Integer movieId;
	}

	@Data
	@Builder
	public static class PaginationRequestDto {
		private Integer page;
		private Integer pageSize;
	}
}

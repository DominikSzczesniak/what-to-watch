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
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		final String urlWithParams = URL + "?page=" + paginationRequestDto.getPage() + "&moviesPerPage=" + paginationRequestDto.getMoviesPerPage();
		return restTemplate.exchange(
				urlWithParams,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {
				}
		);
	}

	public ResponseEntity<PagedMoviesDto> findAllMoviesToWatch(final LoggedUser loggedUser,
															   final String tags,
															   final PaginationRequestDto paginationRequestDto) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdandUserIdHeaders(headers, loggedUser);
		final String urlWithParams = URL + "?tags=" + tags + "&page=" + paginationRequestDto.getPage() + "&moviesPerPage=" + paginationRequestDto.getMoviesPerPage();
		return restTemplate.exchange(
				urlWithParams,
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
		Integer userId;
	}

	@Data
	@Builder
	public static class PaginationRequestDto {
		private Integer page;
		private Integer moviesPerPage;
	}
}

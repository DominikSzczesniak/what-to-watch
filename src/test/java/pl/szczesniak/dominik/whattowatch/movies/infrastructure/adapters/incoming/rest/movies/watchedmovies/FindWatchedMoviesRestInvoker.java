package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest.BaseRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMoviesToWatchRestInvoker.PaginationRequestDto;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import java.util.List;

@Component
public class FindWatchedMoviesRestInvoker extends BaseRestInvoker {

	private static final String URL = "/api/movies/watched";

	FindWatchedMoviesRestInvoker(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	public ResponseEntity<PagedWatchedMoviesDto> findWatchedMovies(final LoggedUser loggedUser,
																   final PaginationRequestDto paginationRequestDto) {
		final HttpHeaders headers = new HttpHeaders();
		addSessionIdHeader(headers, loggedUser);
		final String urlWithParams = URL + "?page=" + paginationRequestDto.getPage() + "&moviesPerPage=" + paginationRequestDto.getPageSize();
		return restTemplate.exchange(
				urlWithParams,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {
				}
		);
	}


	@Value
	public static class PagedWatchedMoviesDto {
		@NonNull List<WatchedMovieDto> movies;

		@NonNull Integer page;

		@NonNull Integer totalPages;

		@NonNull Integer totalMovies;
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	@Builder
	public static class WatchedMovieDto {

		private final String title;
		private final Integer movieId;
		private final Integer userId;

	}

}

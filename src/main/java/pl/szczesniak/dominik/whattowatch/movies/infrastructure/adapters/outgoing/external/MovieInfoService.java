package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MovieInfoService {

	private static final String API_KEY = "4663542b69490ce1434ece35ebad7665";

	private final WebClient webClient;

	public Flux<MovieInfo> getPopularMovies() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/movie/popular")
						.queryParam("api_key", API_KEY)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoResponse.class)
				.flatMapIterable(MovieInfoResponse::getResults);
	}

	public Flux<Genres> getGenres() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/genre/movie/list")
						.queryParam("api_key", API_KEY)
						.build())
				.retrieve()
				.bodyToMono(GenresResponse.class)
				.flatMapIterable(GenresResponse::getGenres);

	}

	public Flux<MovieInfo> getMoviesByGenre(final Long genreId) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/discover/movie")
						.queryParam("api_key", API_KEY)
						.queryParam("with_genres", genreId)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoResponse.class)
				.flatMapIterable(MovieInfoResponse::getResults);
	}

	public Mono<MovieInfo> findMovieById(final Long movieId) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/movie/{movieId}")
						.queryParam("api_key", API_KEY)
						.build(movieId))
				.retrieve()
				.bodyToMono(MovieInfo.class);
	}

}

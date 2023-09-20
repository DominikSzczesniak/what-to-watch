package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TMDBMovieInfoProvider implements MovieInfoApi {

	@Value("${movie.api.key}")
	private String apiKey;

	private final WebClient webClient; // TODO: dorobic httpclient

	@Override
	public MovieInfoResponse getPopularMovies() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/movie/popular")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoResponse.class)
				.block();
	}

	@Override
	public GenresResponse getGenres() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/genre/movie/list")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(GenresResponse.class)
				.block();

	}

	@Override
	public MovieInfoResponse getMoviesByGenre(final Long genreId) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/discover/movie")
						.queryParam("api_key", apiKey)
						.queryParam("with_genres", genreId)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoResponse.class)
				.block();
	}

//	@Override
//	public Mono<MovieInfo> findMovieById(final Long movieId) {
//		return webClient.get()
//				.uri(uriBuilder -> uriBuilder
//						.path("/movie/{movieId}")
//						.queryParam("api_key", apiKey)
//						.build(movieId))
//				.retrieve()
//				.bodyToMono(MovieInfo.class);
//	}

}

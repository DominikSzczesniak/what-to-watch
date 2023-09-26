package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TMDBMovieInfoProvider implements MovieInfoApi {


	@Value("${movie.api.key}")
	private String apiKey;

	private final WebClient webClient;

	@Override
	public TMDBMovieInfoResponse getPopularMovies() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/movie/popular")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(TMDBMovieInfoResponse.class)
				.block();
	}

	@Override
	public TMDBMovieGenreResponse getGenres() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/genre/movie/list")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(TMDBMovieGenreResponse.class)
				.block();

	}

	@Override
	public TMDBMovieInfoResponse getMoviesByGenre(final List<Long> genreId) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/discover/movie")
						.queryParam("api_key", apiKey)
						.queryParam("with_genres", genreId)
						.build())
				.retrieve()
				.bodyToMono(TMDBMovieInfoResponse.class)
				.block();

	}

}

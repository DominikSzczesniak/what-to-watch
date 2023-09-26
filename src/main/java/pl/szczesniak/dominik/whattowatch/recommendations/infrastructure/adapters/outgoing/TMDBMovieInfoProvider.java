package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TMDBMovieInfoProvider implements MovieInfoApi {


	@Value("${movie.api.key}")
	private String apiKey;

	private final WebClient webClient;

	@Override
	public MovieInfoTMDBResponse getPopularMovies() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/movie/popular")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoTMDBResponse.class)
				.block();
	}

	@Override
	public MovieGenreTMDBResponse getGenres() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/genre/movie/list")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(MovieGenreTMDBResponse.class)
				.block();

	}

	@Override
	public MovieInfoTMDBResponse getMoviesByGenre(final List<Long> genreId) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/discover/movie")
						.queryParam("api_key", apiKey)
						.queryParam("with_genres", genreId)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoTMDBResponse.class)
				.block();

	}

}

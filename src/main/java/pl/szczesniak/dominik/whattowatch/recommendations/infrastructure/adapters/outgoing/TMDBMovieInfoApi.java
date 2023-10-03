package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TMDBMovieInfoApi implements MovieInfoApi {

	private final String apiKey;

	private final WebClient webClient;

	TMDBMovieInfoApi(@Value("${tmdb.api.key}") final String apiKey, @Value("${tmdb.base.url}") final String baseUrl) {
		this.apiKey = apiKey;
		this.webClient = WebClient.builder().baseUrl(baseUrl).build();
	}

	@Override
	public MovieInfoResponse getPopularMovies() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/movie/popular")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoResponseDto.class)
				.map(this::mapToMovieInfoResponse)
				.block();
	}

	@Override
	public MovieGenreResponse getGenres() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/genre/movie/list")
						.queryParam("api_key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(MovieGenreResponseDto.class)
				.map(this::mapToMovieGenreResponse)
				.block();

	}

	@Override
	public MovieInfoResponse getMoviesByGenre(final List<Long> genreId) {
		final String finalGenres = prepareGenres(genreId);
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/discover/movie")
						.queryParam("api_key", apiKey)
						.queryParam("with_genres", finalGenres)
						.build())
				.retrieve()
				.bodyToMono(MovieInfoResponseDto.class)
				.map(this::mapToMovieInfoResponse)
				.block();
	}

	private static String prepareGenres(final List<Long> genreId) {
		String genres = "";
		for (Long id : genreId) {
			genres += id + "|";
		}
		return genres.substring(0, genres.length() - 1);
	}

	private MovieGenreResponse mapToMovieGenreResponse(final MovieGenreResponseDto movieGenreResponseDto) {
		final List<MovieGenre> genres = movieGenreResponseDto.getGenres().stream()
				.map(movieGenreDto -> new MovieGenre(
						movieGenreDto.getId(),
						movieGenreDto.getName()
				))
				.collect(Collectors.toList());
		return new MovieGenreResponse(genres);
	}

	private MovieInfoResponse mapToMovieInfoResponse(final MovieInfoResponseDto movieInfoResponseDto) {
		List<MovieInfo> movieInfos = new ArrayList<>();

		if (movieInfoResponseDto.getResults() != null && !movieInfoResponseDto.getResults().isEmpty()) {
			movieInfos = movieInfoResponseDto.getResults().stream()
					.map(movieInfoDto -> new MovieInfo(
							movieInfoDto.getId(),
							movieInfoDto.getGenre_ids(),
							movieInfoDto.getOverview(),
							movieInfoDto.getTitle()
					))
					.collect(Collectors.toList());
		}

		return new MovieInfoResponse(movieInfos);
	}

	@Data
	private static class MovieInfoResponseDto {
		private List<MovieInfoDto> results;
	}

	@Data
	private static class MovieInfoDto {
		private Long id;

		private List<Long> genre_ids;

		private String overview;

		private String title;
	}

	@Data
	private static class MovieGenreResponseDto {
		private List<MovieGenreDto> genres;
	}

	@Data
	private static class MovieGenreDto {
		private Long id;

		private String name;
	}

}

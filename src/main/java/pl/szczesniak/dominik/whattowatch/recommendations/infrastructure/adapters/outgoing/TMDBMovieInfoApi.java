package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoApis;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Component
@Slf4j
class TMDBMovieInfoApi implements MovieInfoApi {

	private final Map<Long, MovieGenre> assignedGenreIds;

	private final String apiKey;
	private final WebClient webClient;

	TMDBMovieInfoApi(@Value("${tmdb.api.key}") final String apiKey, @Value("${tmdb.base.url}") final String baseUrl) {
		this.apiKey = apiKey;
		this.webClient = WebClient.builder().baseUrl(baseUrl).build();
		this.assignedGenreIds = new HashMap<>();
		assignedGenreIds.put(28L, MovieGenre.ACTION);
		assignedGenreIds.put(12L, MovieGenre.ADVENTURE);
		assignedGenreIds.put(16L, MovieGenre.ANIMATION);
		assignedGenreIds.put(35L, MovieGenre.COMEDY);
		assignedGenreIds.put(80L, MovieGenre.CRIME);
		assignedGenreIds.put(99L, MovieGenre.DOCUMENTARY);
		assignedGenreIds.put(18L, MovieGenre.DRAMA);
		assignedGenreIds.put(10751L, MovieGenre.FAMILY);
		assignedGenreIds.put(14L, MovieGenre.FANTASY);
		assignedGenreIds.put(36L, MovieGenre.HISTORY);
		assignedGenreIds.put(27L, MovieGenre.HORROR);
		assignedGenreIds.put(10402L, MovieGenre.MUSIC);
		assignedGenreIds.put(9648L, MovieGenre.MYSTERY);
		assignedGenreIds.put(10749L, MovieGenre.ROMANCE);
		assignedGenreIds.put(878L, MovieGenre.SCIENCE_FICTION);
		assignedGenreIds.put(10770L, MovieGenre.TV_MOVIE);
		assignedGenreIds.put(53L, MovieGenre.THRILLER);
		assignedGenreIds.put(10752L, MovieGenre.WAR);
		assignedGenreIds.put(37L, MovieGenre.WESTERN);
	}

	@Override
	public MovieInfoResponse getPopularMovies() {
		try {
			return webClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/movie/popular")
							.queryParam("api_key", apiKey)
							.build())
					.retrieve()
					.bodyToMono(MovieInfoResponseDto.class)
					.map(this::mapToMovieInfoResponse)
					.block();
		} catch (Exception e) {
			log.info("Unable to fetch movies from external API.");
			return new MovieInfoResponse(emptyList());
		}
	}

	@Override
	public MovieGenreResponse getGenres() {
		try {
			return webClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/genre/movie/list")
							.queryParam("api_key", apiKey)
							.build())
					.retrieve()
					.bodyToMono(MovieGenreResponseDto.class)
					.map(this::mapToMovieGenreResponse)
					.block();
		} catch (Exception e) {
			log.info("Unable to fetch genres from external API.");
			return new MovieGenreResponse(emptyMap());
		}

	}

	private MovieGenreResponse mapToMovieGenreResponse(final MovieGenreResponseDto movieGenreResponseDto) {
		final Map<Long, MovieGenre> genres = new HashMap<>();
		final List<MovieGenreDto> genresDto = movieGenreResponseDto.getGenres();
		genresDto.forEach(genre -> genres.put(genre.getId(), assignedGenreIds.get(genre.getId())));

		return new MovieGenreResponse(genres);
	}

	@Override
	public MovieInfoResponse getMoviesByGenre(final Set<MovieGenre> genres) {
		try {
			final List<Long> genreIds = mapGenreNamesToApiIds(genres);
			final String finalGenres = prepareGenresForRequest(genreIds);
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
		} catch (Exception e) {
			log.info("Fetching recommendations failed, next scheduler should handle recommendation.");
			return new MovieInfoResponse(emptyList());
		}
	}

	private List<Long> mapGenreNamesToApiIds(final Set<MovieGenre> genres) {
		return genres.stream()
				.filter(assignedGenreIds::containsValue)
				.map(genre -> assignedGenreIds.entrySet().stream()
						.filter(apiGenreEntry -> apiGenreEntry.getValue().equals(genre))
						.findFirst()
						.map(Map.Entry::getKey)
						.orElse(null))
				.collect(Collectors.toList());
	}

	private static String prepareGenresForRequest(final List<Long> genreId) {
		String genres = "";
		for (Long id : genreId) {
			genres += id + "|";
		}
		return genres.substring(0, genres.length() - 1);
	}

	private MovieInfoResponse mapToMovieInfoResponse(final MovieInfoResponseDto movieInfoResponseDto) {
		List<MovieInfo> movieInfos = new ArrayList<>();

		if (movieInfoResponseDto.getResults() != null && !movieInfoResponseDto.getResults().isEmpty()) {
			movieInfos = movieInfoResponseDto.getResults().stream()
					.map(movieInfoDto -> new MovieInfo(
							movieInfoDto.getGenre_ids().stream().map(assignedGenreIds::get).collect(Collectors.toList()),
							movieInfoDto.getOverview(),
							movieInfoDto.getTitle(),
							movieInfoDto.getId(),
							MovieInfoApis.TMDB))
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
		private String title;
		private String overview;
		private List<Long> genre_ids;
		private Integer id;
		private Long popularity;
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

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecommendationService {

	private final RecommendationConfigurationManager configurationManager;
	private final MovieInfoApi movieInfoApi;
	private final RecommendedMoviesRepository repository;

	private static final int NUMBER_OF_MOVIES_TO_RECOMMEND = 2;

	public MovieInfoResponse recommendPopularMovies() {
		return movieInfoApi.getPopularMovies();
	}

	@Transactional
	public RecommendedMovies recommendMoviesByConfiguration(final UserId userId) {
		final RecommendationConfiguration configuration = configurationManager.findBy(userId);
		final List<MovieInfo> moviesByConfig = getMovieInfosByConfig(configuration);
		final List<MovieInfo> latestRecommendedMovies = repository.findLatestRecommendedMovies(userId)
				.map(RecommendedMovies::getMovies)
				.orElse(Collections.emptyList());

		final List<MovieInfo> moviesToRecommend = getRecommendedMoviesBySharedGenres(configuration.getGenres(), moviesByConfig, latestRecommendedMovies);

		final RecommendedMovies recommendation = new RecommendedMovies(moviesToRecommend, userId);
		repository.create(recommendation);

		return recommendation;
	}

	public RecommendedMovies findLatestRecommendedMovies(final UserId userId) {
		return repository.findLatestRecommendedMovies(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("No movies to recommend for user with id:" + userId.getValue()));
	}

	public List<MovieGenre> getMovieGenres() {
		return Arrays.stream(MovieGenre.values()).toList();
	}

	public boolean hasRecommendedMoviesForCurrentInterval(final UserId userId) {
		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime intervalStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY)).with(LocalTime.parse("00:00:00"));
		final LocalDateTime intervalEnd = now.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).with(LocalTime.parse("23:59:59"));

		return repository.existsByUserIdAndRecommendationDateBetween(userId, intervalStart, intervalEnd);
	}

	private List<MovieInfo> getMovieInfosByConfig(final RecommendationConfiguration configuration) {
		final List<Long> userGenresIds = mapGenreNamesToApiIds(configuration.getGenres());
		final MovieInfoResponse moviesByGenre = movieInfoApi.getMoviesByGenre(userGenresIds);
		return moviesByGenre.getResults();
	}

	private List<Long> mapGenreNamesToApiIds(final Set<MovieGenre> genres) {
		final Map<Long, MovieGenre> genreMapFromApi = movieInfoApi.getGenres().getGenres();

		return genres.stream()
				.filter(genreMapFromApi::containsValue)
				.map(genre -> genreMapFromApi.entrySet().stream()
						.filter(apiGenreEntry -> apiGenreEntry.getValue().equals(genre))
						.findFirst()
						.map(Map.Entry::getKey)
						.orElse(null))
				.collect(Collectors.toList());
	}

	private static List<MovieInfo> getRecommendedMoviesBySharedGenres(final Set<MovieGenre> configGenres,
																	  final List<MovieInfo> moviesByConfig,
																	  final List<MovieInfo> latestRecommendedMovies) {
		final Map<MovieInfo, Long> sharedGenresCountMap = new HashMap<>();
		for (MovieInfo movie : moviesByConfig) {
			long sharedGenresCount = movie.getGenres().stream()
					.filter(configGenres::contains)
					.count();
			sharedGenresCountMap.put(movie, sharedGenresCount);
		}

		final List<MovieInfo> sortedMoviesByGenreCount = sharedGenresCountMap.keySet().stream()
				.sorted(comparingLong(movie -> -sharedGenresCountMap.get(movie)))
				.collect(Collectors.toList());

		sortedMoviesByGenreCount.removeAll(latestRecommendedMovies);

		return sortedMoviesByGenreCount.subList(0, Math.min(sortedMoviesByGenreCount.size(), NUMBER_OF_MOVIES_TO_RECOMMEND));
	}

}

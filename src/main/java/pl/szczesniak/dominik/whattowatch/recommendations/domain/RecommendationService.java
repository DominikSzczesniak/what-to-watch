package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;

/**
 * RecommendationService handles the recommendation of movies based on user's configurations and previous recommendations.
 * It uses a set of components, including a RecommendationConfigurationManager for user-specific configurations,
 * a MovieInfoApi for retrieving movie information from external API, and a RecommendedMoviesRepository for data storage.
 * <p>
 * Public Methods:
 * - recommendPopularMovies(): Recommends popular movies using the MovieInfoApi.
 * - recommendMoviesByConfiguration(UserId userId): Recommends movies based on the user's configuration, taking into account
 * genres and previous recommendations.
 * - findLatestRecommendedMovies(): Retrieves the latest recommended movies for a user.
 * - getMovieGenres(): Retrieves a list of available movie genres.
 * <p>
 * Private Methods:
 * - getMovieInfosByConfig(RecommendationConfiguration configuration): Retrieves movies information from external api
 * based on the user's configuration.
 * - mapGenreNamesToIds(): Maps movie genres to their corresponding IDs based on the MovieInfoApi interface.
 * - getRecommendedMoviesBySharedGenres(): Filters and sorts movies based on the amount of shared genres, excluding those already recommended.
 * Returns 2 movies with most shared genres.
 */

@RequiredArgsConstructor
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

	private List<MovieInfo> getMovieInfosByConfig(final RecommendationConfiguration configuration) {
		final List<Long> userGenres = mapGenreNamesToIds(configuration.getGenres());
		final MovieInfoResponse moviesByGenre = movieInfoApi.getMoviesByGenre(userGenres);
		return moviesByGenre.getResults();
	}

	private List<Long> mapGenreNamesToIds(final Set<MovieGenre> genres) {
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

	private static List<MovieInfo> getRecommendedMoviesBySharedGenres(final Set<MovieGenre> genres,
																	  final List<MovieInfo> allMovies,
																	  final List<MovieInfo> latestRecommendedMovies) {
		final Map<MovieInfo, Long> sharedGenresCountMap = new HashMap<>();
		for (MovieInfo movie : allMovies) {
			long sharedGenresCount = movie.getGenres().stream()
					.filter(genres::contains)
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

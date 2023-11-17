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
 * Public Methods:
 * recommendPopularMovies():
 * - Calls movieInfoApi.getPopularMovies() to retrieve information about 20 most popular movies.
 * - Returns a MovieInfoResponse object containing details about fetched movies (title, overview, movie genres).
 * - The method can be invoked by the user through a URL request.
 *
 * recommendMoviesByConfiguration(UserId userId):
 * - Retrieves the user's recommendation configuration using RecommendationConfigurationManager.
 * - Calls getMovieInfosByConfig() to fetch MovieInfos (title, overview, movie genres) based on the user's configuration.
 * - Retrieves the user's latest recommended movies from the repository.
 * - Calls getRecommendedMoviesBySharedGenres() that takes user genres, movies found by genres and latest recommended movies as parameters.
 * Filters and sorts movies based on most shared genres. Returns list of movie infos to recommend.
 * - Creates a RecommendedMovies object with the movies to recommend and the user ID as well as the date that movies were prepared.
 * - Saves the recommendation in the RecommendedMoviesRepository.
 * - Returns the generated RecommendedMovies object.
 * - This method is called only by RecommendationScheduler in 2 scenarios:
 * for every user every hour, to prepare recommendation if user has not had any preivous recommendations but has config;
 * for every user every Wednesday at 2:30 to prepare recommendation if user has config
 *
 * findLatestRecommendedMovies(UserId userId):
 * - Retrieves the latest recommended movies for a user from the repository.
 * - Throws an ObjectDoesNotExistException if no recommendations are found.
 * - The method can be invoked by the user through a URL request.
 *
 * getMovieGenres():
 * - Returns a list of available movie genres.
 * - The method can be invoked by the user through a URL request.
 *
 * Private Methods:
 * getMovieInfosByConfig(RecommendationConfiguration configuration):
 * - Maps user genres to API genre IDs using mapGenreNamesToApiIds().
 * - Calls movieInfoApi.getMoviesByGenre() to fetch movie information based on user genres.
 * - Returns a list of MovieInfo objects.
 *
 * mapGenreNamesToApiIds(Set<MovieGenre> genres):
 * - Maps movie genres to their corresponding API IDs based on the MovieInfoApi interface.
 * - Filters genres that are not present in the API.
 * - Returns a list of API genre IDs.
 *
 * getRecommendedMoviesBySharedGenres(Set<MovieGenre> configGenres, List<MovieInfo> moviesByConfig, List<MovieInfo> latestRecommendedMovies):
 * - Calculates the count of shared genres for each movie.
 * - Sorts movies based on the count of shared genres in descending order.
 * - Removes movies that have already been recommended to the user.
 * - Returns a sublist of recommended movies (up to 2 movies).
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

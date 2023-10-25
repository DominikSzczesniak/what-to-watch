package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

	private final RecommendationConfigurationManager configurationManager;
	private final MovieInfoApi movieInfoApi;
	private final RecommendedMoviesRepository repository;

	public MovieInfoResponse recommendPopularMovies() {
		return movieInfoApi.getPopularMovies();
	}

	public RecommendedMovies recommendMoviesByConfiguration(final UserId userId) {
		final RecommendationConfiguration configuration = configurationManager.findBy(userId);
		final List<MovieInfo> moviesByGenre = getMovieInfos(configuration);
		final Optional<RecommendedMovies> latestRecommendedMovies = repository.findLatestRecommendedMovies(userId);

		final List<MovieInfo> moviesToRecommend = latestRecommendedMovies
				.map(recommendedMovies -> getMoviesToRecommend(configuration.getGenres(), moviesByGenre, recommendedMovies.getMovies()))
				.orElseGet(() -> getMoviesToRecommend(configuration.getGenres(), moviesByGenre, new ArrayList<>()));

		final RecommendedMovies recommendation = new RecommendedMovies(moviesToRecommend, userId);
		repository.create(recommendation);

		return recommendation;
	}

	private List<MovieInfo> getMovieInfos(final RecommendationConfiguration configuration) {
		final List<Long> userGenres = mapGenresToIds(configuration.getGenres());
		final MovieInfoResponse moviesByGenre = movieInfoApi.getMoviesByGenre(userGenres);
		return moviesByGenre.getResults();
	}

	private static List<MovieInfo> getMoviesToRecommend(final Set<MovieGenre> genres,
														final List<MovieInfo> allMovies,
														final List<MovieInfo> latestRecommendedMovies) {
		final Map<MovieInfo, Long> sharedGenresCountMap = new HashMap<>();
		for (MovieInfo movie : allMovies) {
			long sharedGenresCount = movie.getGenres().stream()
					.filter(genres::contains)
					.count();
			sharedGenresCountMap.put(movie, sharedGenresCount);
		}

		final List<MovieInfo> sortedMovies = sharedGenresCountMap.entrySet().stream()
				.sorted(Comparator.comparingLong(entry -> -entry.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());

		sortedMovies.removeAll(latestRecommendedMovies);

		return sortedMovies.subList(0, Math.min(sortedMovies.size(), 2));
	}

	private List<Long> mapGenresToIds(final Set<MovieGenre> genres) {
		final Map<Long, MovieGenre> genreMap = movieInfoApi.getGenres().getGenres();
		final List<Long> genreIds = new ArrayList<>();

		for (MovieGenre genre : genres) {
			for (Map.Entry<Long, MovieGenre> entry : genreMap.entrySet()) {
				if (entry.getValue() == genre) {
					genreIds.add(entry.getKey());
				}
			}
		}

		return genreIds;
	}

}

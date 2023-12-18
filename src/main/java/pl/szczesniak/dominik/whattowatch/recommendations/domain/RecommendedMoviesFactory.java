package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;

@Component
class RecommendedMoviesFactory {

	private final int NUMBER_OF_MOVIES_TO_RECOMMEND;

	RecommendedMoviesFactory(@Value("${number.of.movies.to.recommend}") int numberOfMoviesToRecommend) {
		this.NUMBER_OF_MOVIES_TO_RECOMMEND = numberOfMoviesToRecommend;
	}

	RecommendedMovies createNewRecommendedMovies(final Set<MovieGenre> configGenres,
												 final List<MovieInfo> recommendedFromApi,
												 final List<MovieInfo> latestRecommendedMovies, // todo: na latest moze
												 final UserId userId) {

		validateInput(configGenres, recommendedFromApi);

		final Map<MovieInfo, Long> sharedGenresCountMap = new HashMap<>();
		for (MovieInfo movie : recommendedFromApi) {
			long sharedGenresCount = movie.getGenres().stream()
					.filter(configGenres::contains)
					.count();
			sharedGenresCountMap.put(movie, sharedGenresCount);
		}

		final List<MovieInfo> sortedMoviesByGenreCount = sharedGenresCountMap.keySet().stream()
				.sorted(comparingLong(movie -> -sharedGenresCountMap.get(movie)))
				.collect(Collectors.toList());

		sortedMoviesByGenreCount.removeAll(latestRecommendedMovies);
		if (sortedMoviesByGenreCount.size() == 0) {
			throw new ObjectDoesNotExistException("No movies that are not duplicates of previously recommended movies exist.");
		}

		List<MovieInfo> movieInfos = sortedMoviesByGenreCount.subList(0, Math.min(sortedMoviesByGenreCount.size(), NUMBER_OF_MOVIES_TO_RECOMMEND));
		return new RecommendedMovies(movieInfos, userId);
	}

	private static void validateInput(final Set<MovieGenre> configGenres, final List<MovieInfo> recommendedFromApi) {
		if (recommendedFromApi == null || recommendedFromApi.size() == 0) {
			throw new ObjectDoesNotExistException("Database doesn't work");
		}

		if (configGenres == null) {
			throw new ObjectDoesNotExistException("Configuration genres cannot be null");
		}
	}

}

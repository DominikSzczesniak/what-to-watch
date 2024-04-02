package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;

@Entity
@Table
@Getter
@NoArgsConstructor
@ToString
class UserMoviesRecommendations extends BaseEntity {

	@OneToOne
	@JoinTable(
			name = "user_recommendations_movies",
			joinColumns = {
					@JoinColumn(name = "id")
			}
	)
	private RecommendedMovies currentRecommendation;

	private LocalDateTime latestRecommendationDate;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	@Version
	private Integer version;

	UserMoviesRecommendations(@NonNull final UserId userId) {
		this.userId = userId;
		this.latestRecommendationDate = LocalDateTime.now();
	}

	RecommendedMovies recommendMovies(final List<MovieInfo> recommendedFromApi,
									  final Set<MovieGenre> configGenres,
									  final int numberOfMoviesToRecommend) {
		validateInput(configGenres, recommendedFromApi);

		final Map<MovieInfo, Long> sharedGenresCountMap = countSharedGenresPerMovie(recommendedFromApi, configGenres);
		final List<MovieInfo> moviesToRecommend = sortMoviesByGenreCount(sharedGenresCountMap);

		if (moviesToRecommend.size() == 0) {
			throw new ObjectDoesNotExistException("No movies that are not duplicates of previously recommended movies exist.");
		}

		final List<MovieInfo> movieInfos = moviesToRecommend.subList(0, Math.min(moviesToRecommend.size(), numberOfMoviesToRecommend));
		final RecommendedMovies recommendedMovies = new RecommendedMovies(movieInfos, userId);
		updateCurrentRecommendation(recommendedMovies);

		return recommendedMovies;
	}

	private List<MovieInfo> sortMoviesByGenreCount(final Map<MovieInfo, Long> sharedGenresCountMap) {
		final List<MovieInfo> sortedMoviesByGenreCount = sharedGenresCountMap.keySet().stream()
				.sorted(comparingLong(movie -> -sharedGenresCountMap.get(movie)))
				.collect(Collectors.toList());

		sortedMoviesByGenreCount.removeAll(getCurrentRecommendation()
				.map(RecommendedMovies::getMovies)
				.orElse(Collections.emptyList())
		);
		return sortedMoviesByGenreCount;
	}

	private static Map<MovieInfo, Long> countSharedGenresPerMovie(final List<MovieInfo> recommendedFromApi, final Set<MovieGenre> configGenres) {
		final Map<MovieInfo, Long> sharedGenresCountMap = new HashMap<>();
		for (MovieInfo movie : recommendedFromApi) {
			long sharedGenresCount = movie.getGenres().stream()
					.filter(configGenres::contains)
					.count();
			sharedGenresCountMap.put(movie, sharedGenresCount);
		}
		return sharedGenresCountMap;
	}

	private static void validateInput(final Set<MovieGenre> configGenres, final List<MovieInfo> recommendedFromApi) {
		if (recommendedFromApi == null || recommendedFromApi.size() == 0) {
			throw new ObjectDoesNotExistException("Database doesn't work");
		}

		if (configGenres == null) {
			throw new ObjectDoesNotExistException("Configuration genres cannot be null");
		}
	}

	private void updateCurrentRecommendation(final RecommendedMovies recommendedMovies) {
		currentRecommendation = recommendedMovies;
		this.latestRecommendationDate = LocalDateTime.now();
	}

	Optional<RecommendedMovies> getCurrentRecommendation() {
		return Optional.ofNullable(currentRecommendation);
	}

}

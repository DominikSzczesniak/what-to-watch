package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;

@Entity
@Table(name = "user_movies_recommendations", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id"})
})
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

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	@Version
	private Integer version;

	UserMoviesRecommendations(@NonNull final UserId userId) {
		this.userId = userId;
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
	}

	Optional<RecommendedMovies> getCurrentRecommendation() {
		return Optional.ofNullable(currentRecommendation);
	}

	boolean hasRecommendedMoviesForCurrentInterval(final Clock clock) {
		if (currentRecommendation == null) {
			return false;
		}
		final LocalDateTime now = LocalDateTime.now(clock);
		final LocalDateTime intervalStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY)).with(LocalTime.parse("00:00:00"));
		final LocalDateTime intervalEnd = now.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).with(LocalTime.parse("23:59:59"));

		final LocalDateTime latestRecommendationDate = currentRecommendation.getCreationDate();

		return latestRecommendationDate != null &&
				latestRecommendationDate.isAfter(intervalStart) &&
				latestRecommendationDate.isBefore(intervalEnd);
	}

}

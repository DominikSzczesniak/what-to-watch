package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryRecommendedMoviesRepository implements RecommendedMoviesRepository {

	private final AtomicInteger nextId = new AtomicInteger(0);
	private final Map<RecommendedMoviesId, RecommendedMovies> movies = new HashMap<>();

	@Override
	public RecommendedMoviesId create(final RecommendedMovies recommendedMovies) {
		final int id = nextId.incrementAndGet();
		final Long idAsLong = (long) id;
		try {
			setId(recommendedMovies, idAsLong);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		movies.put(new RecommendedMoviesId(idAsLong), recommendedMovies);
		return recommendedMovies.getRecommendedMoviesId();
	}

	private static void setId(final RecommendedMovies recommendedMovies, final Long idAsLong) throws NoSuchFieldException, IllegalAccessException {
		final Class<RecommendedMovies> recommendationConfigurationClass = RecommendedMovies.class;
		final Field recommendedMoviesId = recommendationConfigurationClass.getDeclaredField("recommendedMoviesId");
		recommendedMoviesId.setAccessible(true);
		recommendedMoviesId.set(recommendedMovies, idAsLong);
	}

	@Override
	public Optional<RecommendedMovies> findLatestRecommendedMovies(final UserId userId) {
		RecommendedMovies latestRecommendedMovies = null;
		LocalDateTime latestDate = LocalDateTime.MIN;

		for (RecommendedMovies recommendedMovies : movies.values()) {
			if (recommendedMovies.getUserId().equals(userId)) {
				final LocalDateTime recommendedDate = recommendedMovies.getCreationDate();
				if (recommendedDate.isAfter(latestDate) || recommendedDate.isEqual(latestDate)) {
					latestDate = recommendedDate;
					latestRecommendedMovies = recommendedMovies;
				}
			}
		}

		return Optional.ofNullable(latestRecommendedMovies);
	}

	@Override
	public boolean existsByUserIdAndRecommendationDateBetween(final UserId userId,
															  final LocalDateTime intervalStart,
															  final LocalDateTime intervalEnd) {
		return movies.values().stream()
				.anyMatch(recommendedMovies -> recommendedMovies.getUserId().equals(userId) &&
						recommendedMovies.getCreationDate().isBefore(intervalEnd) &&
						recommendedMovies.getEndInterval().isAfter(intervalStart)
				);
	}

}

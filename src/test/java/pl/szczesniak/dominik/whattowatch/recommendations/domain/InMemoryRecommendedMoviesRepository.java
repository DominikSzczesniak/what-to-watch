package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryRecommendedMoviesRepository implements RecommendedMoviesQueryService {

	private final Map<RecommendedMoviesId, RecommendedMovies> movies = new HashMap<>();

	@Override
	public Optional<RecommendedMoviesQueryResult> findLatestRecommendedMoviesQueryResult(final UserId userId) {
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

		RecommendedMoviesQueryResult recommendedMoviesQueryResult = null;
		if (latestRecommendedMovies != null) {
			recommendedMoviesQueryResult = new RecommendedMoviesQueryResult(
					latestRecommendedMovies.getId(),
					latestRecommendedMovies.getMovies(),
					latestRecommendedMovies.getCreationDate(),
					latestRecommendedMovies.getEndInterval()
			);
		}

		return Optional.ofNullable(recommendedMoviesQueryResult);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryUserRecommendationsRepository implements RecommendationsRepository {

	private final Map<UserId, UserMoviesRecommendations> recommendations = new HashMap<>();

	@Override
	public Optional<UserMoviesRecommendations> findBy(final UserId userId) {
		return Optional.ofNullable(recommendations.get(userId));
	}

	@Override
	public void create(final UserMoviesRecommendations userMoviesRecommendations) {
		recommendations.put(userMoviesRecommendations.getUserId(), userMoviesRecommendations);
	}

	@Override
	public void update(final UserMoviesRecommendations userMoviesRecommendations) {
		recommendations.replace(userMoviesRecommendations.getUserId(), userMoviesRecommendations);
	}

}

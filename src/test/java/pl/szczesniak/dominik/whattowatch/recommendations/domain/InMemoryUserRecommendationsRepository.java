package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryUserRecommendationsRepository implements RecommendationsRepository {

	private final HashMap<UserId, UserMoviesRecommendations> aggregates = new HashMap<>();

	@Override
	public Optional<UserMoviesRecommendations> findBy(final UserId userId) {
		return Optional.ofNullable(aggregates.get(userId));
	}

	@Override
	public void create(final UserMoviesRecommendations userMoviesRecommendations) {
		aggregates.put(userMoviesRecommendations.getUserId(), userMoviesRecommendations);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
class UserDeletedRecommendationsService {

	private final RecommendationConfigurationRepository recommendationConfigurationRepository;

	public void removeAllDeletedUsersData(final UserId userId) {
		recommendationConfigurationRepository.removeAllBy(userId);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Service
@RequiredArgsConstructor
class UserDeletedRecommendationsService {

	private final RecommendationConfigurationRepository recommendationConfigurationRepository;
	private final RecommendedMoviesRepository recommendedMoviesRepository;

	@Transactional
	public void removeAllDeletedUsersData(final UserId userId) {
		recommendationConfigurationRepository.removeAllBy(userId);
		recommendedMoviesRepository.removeAllBy(userId);
	}

}

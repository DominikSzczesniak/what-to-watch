package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@RequiredArgsConstructor
class RecommendationConfigurationManager {

	private final RecommendationConfigurationRepository repository;

	ConfigurationId create(final CreateRecommendationConfiguration command) {
		final RecommendationConfiguration config = new RecommendationConfiguration(command.getLimitToGenres(), command.getUserId());
		return repository.create(config);
	}

	void update(final UpdateRecommendationConfiguration command) {
		final RecommendationConfiguration config = getRecommendationConfigurationBy(command.getUserId());
		config.update(command.getGenres());
		repository.update(config);
	}

	Optional<RecommendationConfiguration> findby(final UserId userId) {
		return repository.findBy(userId);
	}

	RecommendationConfiguration getRecommendationConfigurationBy(final UserId userId) {
		return repository.findBy(userId).orElseThrow(
				() -> new ObjectDoesNotExistException("No configuration found for user with id: " + userId));
	}

}

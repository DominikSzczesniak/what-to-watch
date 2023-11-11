package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

/**
 * Class responsible for saving and fetching data from repository.
 */

@RequiredArgsConstructor
@Component
public class RecommendationConfigurationManager {

	private final RecommendationConfigurationRepository repository;

	public ConfigurationId create(final CreateRecommendationConfiguration command) {
		final RecommendationConfiguration config = new RecommendationConfiguration(command.getGenres(), command.getUserId());
		return repository.save(config);
	}

	public RecommendationConfiguration findBy(final UserId userId) {
		return repository.findBy(userId).orElseThrow(
				() -> new ObjectDoesNotExistException("No configuration found for user with id: " + userId));
	}

	public void updateRecommendationConfiguration(final UpdateRecommendationConfiguration command) {
		final RecommendationConfiguration config = findBy(command.getUserId());
		config.update(command.getGenres());
		repository.save(config);
	}

}

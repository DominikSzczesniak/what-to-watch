package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class RecommendationConfigurationManager { // todo config

	private final RecommendationConfigurationRepository repository;

	public ConfigurationId create(final CreateRecommendationConfiguration command) {
		if (repository.findBy(command.getUserId()).isPresent()) {
			throw new ObjectAlreadyExistsException(
					"Recommendation configuration for this user with userId="
							+ command.getUserId().getValue()
							+ " already exists."
			);
		}
		final RecommendationConfiguration config = new RecommendationConfiguration(command.getGenres(), command.getUserId());
		return repository.create(config);
	}

	public void update(final UpdateRecommendationConfiguration command) {
		final RecommendationConfiguration config = findBy(command.getUserId());
		config.update(command.getGenres());
		repository.update(config);
	}

	public RecommendationConfiguration findBy(final UserId userId) {
		return repository.findBy(userId).orElseThrow(
				() -> new ObjectDoesNotExistException("No configuration found for user with id: " + userId));
	}

	public List<RecommendationConfiguration> findAll() {
		return repository.findAll();
	}

}

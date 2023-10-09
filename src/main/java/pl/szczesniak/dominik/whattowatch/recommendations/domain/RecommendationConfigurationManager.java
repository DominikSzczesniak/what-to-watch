package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class RecommendationConfigurationManager {

	private final RecommendationConfigurationRepository repository;

	public ConfigurationId create(final UserId userId, Set<MovieGenre> genres) {
		final RecommendationConfiguration config = new RecommendationConfiguration(genres, userId);
		repository.create(config);
		return new ConfigurationId(config.getConfigurationId());
	}

	public RecommendationConfiguration findBy(final UserId userId) {
		return repository.findBy(userId).orElseThrow(
				() -> new ObjectDoesNotExistException("No configuration found for user with id: " + userId));
	}

}

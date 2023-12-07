package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryRecommendationConfigurationRepository implements RecommendationConfigurationRepository {

	private final AtomicInteger nextId = new AtomicInteger(0);
	private final Map<UserId, RecommendationConfiguration> configurations = new HashMap<>();

	@Override
	public ConfigurationId create(final RecommendationConfiguration configuration) {
		final int id = nextId.incrementAndGet();
		final Long idAsLong = (long) id;
		configuration.setConfigurationId(idAsLong);
		configurations.put(configuration.getUserId(), configuration);
		return configuration.getConfigurationId();
	}

	@Override
	public ConfigurationId update(final RecommendationConfiguration configuration) {
		configurations.replace(configuration.getUserId(), configuration);
		return configuration.getConfigurationId();
	}

	@Override
	public Optional<RecommendationConfiguration> findBy(final UserId userId) {
		return Optional.ofNullable(configurations.get(userId));
	}

	@Override
	public List<RecommendationConfiguration> findAll() {
		return configurations.values().stream().toList();
	}

}

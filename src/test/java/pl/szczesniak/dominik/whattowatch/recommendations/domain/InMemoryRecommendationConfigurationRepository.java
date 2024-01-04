package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static pl.szczesniak.dominik.whattowatch.recommendations.domain.InMemoryRecommendationConfigurationRepository.PersistedRecommendationConfiguration.toPersisted;

class InMemoryRecommendationConfigurationRepository implements RecommendationConfigurationRepository {

	private final AtomicInteger nextId = new AtomicInteger(0);
	private final Map<UserId, PersistedRecommendationConfiguration> configurations = new HashMap<>();

	@Override
	public ConfigurationId create(final RecommendationConfiguration configuration) {
		final int id = nextId.incrementAndGet();
		final Long idAsLong = (long) id;
		try {
			setId(configuration, idAsLong);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		configurations.put(configuration.getUserId(), toPersisted(configuration));
		return configuration.getConfigurationId();
	}

	@Override
	public ConfigurationId update(final RecommendationConfiguration configuration) {
		configurations.replace(configuration.getUserId(), toPersisted(configuration));
		return configuration.getConfigurationId();
	}

	@Override
	public Optional<RecommendationConfiguration> findBy(final UserId userId) {
		return Optional.ofNullable(configurations.get(userId)).map(PersistedRecommendationConfiguration::fromPersisted);
	}

	@Override
	public List<RecommendationConfiguration> findAll() {
		return configurations.values().stream().map(PersistedRecommendationConfiguration::fromPersisted).toList();
	}

	private static void setId(final RecommendationConfiguration configuration, final Long idAsLong) throws NoSuchFieldException, IllegalAccessException {
		final Class<RecommendationConfiguration> recommendationConfigurationClass = RecommendationConfiguration.class;
		final Field configurationId = recommendationConfigurationClass.getDeclaredField("configurationId");
		configurationId.setAccessible(true);
		configurationId.set(configuration, idAsLong);
	}

	@Value
	@EqualsAndHashCode(of = {"configurationId"})
	static class PersistedRecommendationConfiguration {

		ConfigurationId configurationId;

		Set<MovieGenre> limitToGenres;

		UserId userId;

		static PersistedRecommendationConfiguration toPersisted(final RecommendationConfiguration configuration) {
			return new PersistedRecommendationConfiguration(configuration.getConfigurationId(), configuration.getGenres(), configuration.getUserId());
		}

		static RecommendationConfiguration fromPersisted(final PersistedRecommendationConfiguration persistedConfig) {
			final RecommendationConfiguration recommendationConfiguration = new RecommendationConfiguration(
					persistedConfig.getLimitToGenres(),
					persistedConfig.getUserId()
			);

			setRecommendationConfigurationId(persistedConfig, recommendationConfiguration);
			return recommendationConfiguration;
		}

		private static void setRecommendationConfigurationId(final PersistedRecommendationConfiguration persistedConfig,
															 final RecommendationConfiguration recommendationConfiguration) {
			final Class<RecommendationConfiguration> recommendationConfigurationClass = RecommendationConfiguration.class;
			final Field configurationId;
			try {
				configurationId = recommendationConfigurationClass.getDeclaredField("configurationId");
				configurationId.setAccessible(true);
				configurationId.set(recommendationConfiguration, persistedConfig.getConfigurationId().getValue());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	}

}

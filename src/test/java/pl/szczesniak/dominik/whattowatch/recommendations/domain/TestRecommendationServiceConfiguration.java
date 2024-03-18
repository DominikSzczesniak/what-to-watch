package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import java.time.Clock;

class TestRecommendationServiceConfiguration {

	static RecommendationFacade recommendationFacade(final Clock clock) {
		final InMemoryRecommendationConfigurationRepositoryConfiguration inMemoryRecommendationConfigurationRepository =
				new InMemoryRecommendationConfigurationRepositoryConfiguration();
		final RecommendationConfigurationManager configurationManager = new RecommendationConfigurationManager(inMemoryRecommendationConfigurationRepository);
		final InMemoryRecommendedMoviesRepository repository = new InMemoryRecommendedMoviesRepository();

		final RecommendationService recommendationService = new RecommendationService(
				configurationManager,
				new InMemoryMovieInfoApiRepository(),
				repository,
				new RecommendedMoviesFactory(2),
				clock
		);

		return new RecommendationFacade(
				configurationManager,
				recommendationService,
				inMemoryRecommendationConfigurationRepository,
				repository
		);
	}

}

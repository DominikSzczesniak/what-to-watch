package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import java.time.Clock;

class TestRecommendationServiceConfiguration {

	static RecommendationFacade recommendationFacade(final Clock clock) {
		final InMemoryRecommendationConfigurationRepository inMemoryRecommendationConfigurationRepository = new InMemoryRecommendationConfigurationRepository();
		final RecommendationConfigurationManager configurationManager = new RecommendationConfigurationManager(inMemoryRecommendationConfigurationRepository);
		return new RecommendationFacade(
				configurationManager,
				new RecommendationService(
						configurationManager,
						new InMemoryMovieInfoApiRepository(),
						new InMemoryRecommendedMoviesRepository(),
						new RecommendedMoviesFactory(2),
						clock
				),
				inMemoryRecommendationConfigurationRepository
		);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

public class TestRecommendationServiceConfiguration {

	static RecommendationService recommendationService(final RecommendationConfigurationManager configManager) {
		return new RecommendationService(
				configManager,
				new InMemoryMovieInfoApiRepository(),
				new InMemoryRecommendedMoviesRepository()
		);
	}

}

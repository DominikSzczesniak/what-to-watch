package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;

class TestRecommendationFacadeConfiguration {

	static RecommendationFacade recommendationFacade(final Clock clock) {
		final InMemoryRecommendationConfigurationRepositoryConfiguration inMemoryRecommendationConfigurationRepository =
				new InMemoryRecommendationConfigurationRepositoryConfiguration();
		final RecommendationConfigurationManager configurationManager = new RecommendationConfigurationManager(inMemoryRecommendationConfigurationRepository);
		final InMemoryRecommendedMoviesRepository repository = new InMemoryRecommendedMoviesRepository();
		return new RecommendationFacade(
				configurationManager,
				new RecommendationService(
						new InMemoryMovieInfoApi(),
						new InMemoryUserRecommendationsRepository(),
						clock,
						new DummyTransactionTemplate(),
						2
				),
				inMemoryRecommendationConfigurationRepository,
				repository,
				new UserDeletedRecommendationsService(inMemoryRecommendationConfigurationRepository)
		);
	}

	private static class DummyTransactionTemplate extends TransactionTemplate {

		public DummyTransactionTemplate() {
			super();
		}

		@Override
		public <T> T execute(TransactionCallback<T> action) {
			return action.doInTransaction(null);
		}
	}

}

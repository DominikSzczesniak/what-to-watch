package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.time.Clock;

class TestRecommendationServiceConfiguration {

	static RecommendationFacade recommendationFacade(final Clock clock) {
		final InMemoryRecommendationConfigurationRepositoryConfiguration inMemoryRecommendationConfigurationRepository =
				new InMemoryRecommendationConfigurationRepositoryConfiguration();
		final RecommendationConfigurationManager configurationManager = new RecommendationConfigurationManager(inMemoryRecommendationConfigurationRepository);
		final InMemoryRecommendedMoviesRepository repository = new InMemoryRecommendedMoviesRepository();
		return new RecommendationFacade(
				configurationManager,
				new RecommendationService(
						configurationManager,
						new InMemoryMovieInfoApi(),
						repository,
						new InMemoryUserRecommendationsRepository(),
						clock,
						dummyTransactionManager(),
						2
				),
				inMemoryRecommendationConfigurationRepository,
				repository
		);
	}

	private static PlatformTransactionManager dummyTransactionManager() {
		return new PlatformTransactionManager() {
			@Override
			public TransactionStatus getTransaction(final TransactionDefinition definition) throws TransactionException {
				return null;
			}

			@Override
			public void commit(final TransactionStatus status) throws TransactionException {

			}

			@Override
			public void rollback(final TransactionStatus status) throws TransactionException {

			}
		};
	}

}

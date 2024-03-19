package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

import java.time.Clock;

@Configuration
class RecommendationServiceConfiguration {

	@Value("${number.of.movies.to.recommend}")
	private int numberOfMoviesToRecommend;

	@Bean
	RecommendationService recommendationService(final RecommendationConfigurationManager configurationManager,
												final MovieInfoApi movieInfoApi,
												final RecommendedMoviesRepository repository,
												final RecommendationsRepository recommendationsRepository, final PlatformTransactionManager transactionManager) {
		return new RecommendationService(
				configurationManager,
				movieInfoApi,
				repository,
				recommendationsRepository,
				Clock.systemDefaultZone(),
				transactionManager,
				numberOfMoviesToRecommend
		);
	}

}

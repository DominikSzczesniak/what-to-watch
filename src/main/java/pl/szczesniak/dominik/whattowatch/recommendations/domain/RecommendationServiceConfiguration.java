package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

import java.time.Clock;

@Configuration
class RecommendationServiceConfiguration {

	@Bean
	RecommendationService recommendationService(final RecommendationConfigurationManager configurationManager,
												final MovieInfoApi movieInfoApi,
												final RecommendedMoviesRepository repository,
												final RecommendationsRepository recommendationsRepository,
												final TransactionTemplate transactionTemplate,
												@Value("${number.of.movies.to.recommend}") final int numberOfMoviesToRecommend) {
		return new RecommendationService(
				configurationManager,
				movieInfoApi,
				repository,
				recommendationsRepository,
				Clock.systemDefaultZone(),
				transactionTemplate,
				numberOfMoviesToRecommend
		);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

import java.time.Clock;

@Configuration
class RecommendationServiceConfiguration {

	@Bean
	RecommendationService recommendationService(final RecommendationConfigurationManager configurationManager,
												final MovieInfoApi movieInfoApi,
												final RecommendedMoviesRepository repository,
												final RecommendedMoviesFactory recommendedMoviesFactory) {
		return new RecommendationService(configurationManager, movieInfoApi, repository, recommendedMoviesFactory, Clock.systemDefaultZone());
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

@Configuration
public class RecommendationServiceConfiguration {

	@Bean
	public RecommendationService recommendationService(final RecommendationConfigurationManager configurationManager,
													   final MovieInfoApi movieInfoApi,
													   final RecommendedMoviesRepository repository) {
		return new RecommendationService(configurationManager, movieInfoApi, repository);
	}

}

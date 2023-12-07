package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
@Slf4j
public class RecommendationDecisionHandler {

	private final RecommendationService recommendationService;
	private final RecommendationConfigurationManager recommendationConfigurationManager;

	public void recommendMovies() {
		final List<RecommendationConfiguration> allRecommendationConfigurations = recommendationConfigurationManager.findAll();

		allRecommendationConfigurations.forEach(config -> {
			if (shouldRecommendMovies(config)) {
				recommendationService.recommendMoviesByConfiguration(config.getUserId());
			} else {
				log.info("User with userId: " + config.getUserId().getValue() + " already has recommended movies for this interval");
			}
		});
	}

	private boolean shouldRecommendMovies(final RecommendationConfiguration config) {
		return !recommendationService.hasRecommendedMoviesForCurrentInterval(config.getUserId());
	}

}

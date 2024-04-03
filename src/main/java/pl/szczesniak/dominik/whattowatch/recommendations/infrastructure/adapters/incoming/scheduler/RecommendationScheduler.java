package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RecommendationScheduler {

	private final NewRecommendationsTrigger newRecommendationsTrigger;

	@Scheduled(fixedRateString = "${recommendation.every.hour.scheduler}")
	void recommendMovies() {
		newRecommendationsTrigger.recommendMovies();
	}

}

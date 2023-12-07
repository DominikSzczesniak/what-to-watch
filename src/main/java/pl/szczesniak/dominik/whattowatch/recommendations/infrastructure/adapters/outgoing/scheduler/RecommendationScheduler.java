package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RecommendationScheduler {

	private final RecommendationDecisionHandler recommendationDecisionHandler;

	@Scheduled(fixedRateString = "${recommendation.every.hour.scheduler}")
	void recommendMovies() {
		recommendationDecisionHandler.recommendMovies();
	}

}

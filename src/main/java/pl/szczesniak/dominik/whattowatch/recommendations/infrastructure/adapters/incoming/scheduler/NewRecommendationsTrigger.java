package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.RecommendMovies;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendationDataQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationDataQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
@Slf4j
public class NewRecommendationsTrigger {

	private final RecommendationFacade facade;

	private final RecommendationDataQueryService queryService;

	public void recommendMovies() {
		final List<RecommendationDataQueryResult> allRecommendationDataQueryResult = queryService.findAllRecommendationDataQueryResult();
		allRecommendationDataQueryResult.forEach(recommendation -> facade.recommendMovies(
				new RecommendMovies(new UserId(recommendation.getUserId()), recommendation.getGenres())));
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationService;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

/**
 * Class responsible only for holding the logic for scheduler to recommend movies.
 */

@RequiredArgsConstructor
@Component
public class RecommendationSchedulerHandler {

	private final RecommendationService recommendationService;
	private final UserService userService;

	public void recommendMoviesWeekly() {
		final List<UserId> userIDs = userService.findAllUsers();

		userIDs.forEach(recommendationService::recommendMoviesByConfiguration);
	}

	public void recommendMoviesEveryHour() {
		final List<UserId> userIDs = userService.findAllUsers();

		userIDs.forEach(userId -> {
			try {
				recommendationService.findLatestRecommendedMovies(userId);
			} catch (ObjectDoesNotExistException e) {
				recommendationService.recommendMoviesByConfiguration(userId);
			}
		});
	}

}

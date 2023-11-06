package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationScheduler {

	private final RecommendationService recommendationService;
	private final UserService userService;

	@Scheduled(cron = "30 2 * * 3 *")
	void recommendMoviesWeekly() {
		final List<UserId> userIDs = userService.findAllUsers();

		for (UserId userId : userIDs) {
			recommendationService.recommendMoviesByConfiguration(userId);
		}
	}

//	@Scheduled(fixedRate = 3600000)
@Scheduled(fixedRate = 10000)
	void recommendMoviesEveryHour() {
		final List<UserId> userIDs = userService.findAllUsers();
		log.info("ruszyl skedzuler");

		for (UserId userId : userIDs) {
			try {
				if (recommendationService.findLatestRecommendedMoviesScheduler(userId).isEmpty()) {
					recommendationService.recommendMoviesByConfiguration(userId);
				}
			} catch (ObjectDoesNotExistException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}

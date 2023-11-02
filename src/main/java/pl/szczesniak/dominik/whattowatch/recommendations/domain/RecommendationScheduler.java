package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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

	@Scheduled(fixedRate = 10000) // TODO: fix, zrobic ze sie rekomenduja tylko jak null
	void recommendMoviesEveryHour() {
		final List<UserId> userIDs = userService.findAllUsers();
		log.info("ruszyl skedzuler");

		for (UserId userId : userIDs) {
			log.info("zlapal blad");
			recommendationService.recommendMoviesByConfiguration(userId);
		}
	}

}

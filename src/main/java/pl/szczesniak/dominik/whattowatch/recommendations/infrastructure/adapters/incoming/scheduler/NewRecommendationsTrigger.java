package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
@Slf4j
public class NewRecommendationsTrigger {

	private final RecommendationFacade facade;

	public void recommendMovies() { // todo:
		final List<UserId> usersWithRecommendationConfiguration = facade.findAllUsersWithRecommendationConfiguration();
		// zrobic joina i pobrac userid, configuration-genres, latestRecommendationVersion(optional)
		usersWithRecommendationConfiguration.forEach(facade::recommendMoviesByConfiguration);
	}

}

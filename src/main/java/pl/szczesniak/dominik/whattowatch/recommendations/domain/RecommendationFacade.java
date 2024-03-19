package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendationConfigurationQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecommendationFacade {

	private final RecommendationConfigurationManager configurationManager;
	private final RecommendationService recommendationService;
	private final RecommendationConfigurationQueryService recommendationConfigurationQueryService;
	private final RecommendedMoviesQueryService recommendedMoviesQueryService;
	private final UserDeletedRecommendationsService userDeletedRecommendationsService;

	public MovieInfoResponse recommendPopularMovies() {
		return recommendationService.recommendPopularMovies();
	}

	public void recommendMoviesByConfiguration(final UserId userId) {
		recommendationService.recommendMoviesByConfiguration(userId);
	}

	public RecommendedMoviesQueryResult getLatestRecommendedMovies(final UserId userId) {
		return recommendedMoviesQueryService.findLatestRecommendedMoviesQueryResult(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("No recommended movies for user"));
	}


	public ConfigurationId create(final CreateRecommendationConfiguration command) {
		return configurationManager.create(command);
	}

	public void update(final UpdateRecommendationConfiguration command) {
		configurationManager.update(command);
	}

	public RecommendationConfigurationRequestResult getRecommendationConfiguration(final UserId userId) {
		return recommendationConfigurationQueryService.findRecommendationConfigurationQueryResultBy(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("No recommended movies for user with id " + userId.getValue()));
	}

	public List<UserId> findAllUsersWithRecommendationConfiguration() {
		return recommendationConfigurationQueryService.findAllUsersWithRecommendationConfigurations();
	}

	public void handleUserDeleted(final UserId userId) {
		userDeletedRecommendationsService.removeAllDeletedUsersData(userId);
	}

}

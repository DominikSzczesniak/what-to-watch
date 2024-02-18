package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query.RecommendationQueryService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecommendationFacade {

	private final RecommendationConfigurationManager configurationManager;

	private final RecommendationService recommendationService;

	private final RecommendationQueryService recommendationQueryService;

	public MovieInfoResponse recommendPopularMovies() {
		return recommendationService.recommendPopularMovies();
	}

	public void recommendMoviesByConfiguration(final UserId userId) {
		recommendationService.recommendMoviesByConfiguration(userId);
	}

	public RecommendedMovies getLatestRecommendedMovies(final UserId userId) {
		return recommendationService.getLatestRecommendedMovies(userId);
	}

	public boolean hasRecommendedMoviesForCurrentInterval(final UserId userId) {
		return recommendationService.hasRecommendedMoviesForCurrentInterval(userId);
	}

	public ConfigurationId create(final CreateRecommendationConfiguration command) {
		return configurationManager.create(command);
	}

	public void update(final UpdateRecommendationConfiguration command) {
		configurationManager.update(command);
	}

	public RecommendationConfigurationRequestResult findBy(final UserId userId) {
		return recommendationQueryService.findRecommendationConfigurationQueryResultBy(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("No recommended movies for user with id " + userId.getValue()));
	}

	public List<UserId> findAllUsersWithRecommendationConfiguration() {
		return recommendationQueryService.findAllUsersWithRecommendationConfigurations();
	}

}

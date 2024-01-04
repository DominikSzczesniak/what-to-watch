package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecommendationFacade {

	private final RecommendationConfigurationManager configurationManager;

	private final RecommendationService recommendationService;

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

	public RecommendationConfiguration findBy(final UserId userId) {
		return configurationManager.findBy(userId);
	}

	public List<RecommendationConfiguration> findAllRecommendationConfigurations() {
		return configurationManager.findAll();
	}

	public List<UserId> findAllUsersWithRecommendationConfiguration() {
		return configurationManager.findAllUsersWithRecommendationConfigurations();
	}

}

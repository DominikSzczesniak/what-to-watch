package pl.szczesniak.dominik.whattowatch.recommendations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.GetRecommendationConfigurationInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.GetRecommendationConfigurationInvoker.RecommendationConfigurationDto;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations.CreateRecommendationConfigurationInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations.CreateRecommendationConfigurationInvoker.CreateRecommendationConfigurationDto;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations.GetMovieGenresInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations.UpdateRecommendationConfigurationInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies.GetLatestRecommendedMoviesInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.scheduler.RecommendationDecisionHandler;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations.GetMovieGenresInvoker.MovieGenresDto;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.configurations.UpdateRecommendationConfigurationInvoker.UpdateRecommendationConfigurationDto;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies.GetLatestRecommendedMoviesInvoker.MovieInfoDto;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies.GetLatestRecommendedMoviesInvoker.RecommendedMoviesDto;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecommendationsModuleIntegrationTest {

	@Autowired
	private RecommendationDecisionHandler recommendationDecisionHandler;

	@Autowired
	private GetMovieGenresInvoker getMovieGenresRest;

	@Autowired
	private CreateRecommendationConfigurationInvoker createRecommendationConfigurationRest;

	@Autowired
	private GetRecommendationConfigurationInvoker getRecommendationConfigurationRest;

	@Autowired
	private GetLatestRecommendedMoviesInvoker getLatestRecommendedMoviesRest;

	@Autowired
	private UpdateRecommendationConfigurationInvoker updateRecommendationConfigurationRest;

	@Autowired
	private LoggedUserProvider loggedUserProvider;

	@Test
	void should_create_and_find_recommendation_configuration() {
		// given
		final LoggedUser loggedUser = loggedUserProvider.getLoggedUser();

		// when
		final ResponseEntity<MovieGenresDto> getMovieGenresResponse = getMovieGenresRest.getMovieGenres(loggedUser);

		// then
		assertThat(getMovieGenresResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getMovieGenresResponse.getBody().getGenresNames()).contains("WAR");

		// given
		final List<String> genreNames = List.of("WAR");

		// when
		final ResponseEntity<Long> createRecommendationResponse = createRecommendationConfigurationRest.createRecommendationConfiguration(
				loggedUser, CreateRecommendationConfigurationDto.builder().limitToGenres(genreNames).build());

		// then
		assertThat(createRecommendationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<RecommendationConfigurationDto> recommendationConfiguration =
				getRecommendationConfigurationRest.getRecommendationConfiguration(loggedUser);

		// then
		assertThat(recommendationConfiguration.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(recommendationConfiguration.getBody().getConfigurationId()).isEqualTo(createRecommendationResponse.getBody());
		assertThat(recommendationConfiguration.getBody().getGenreNames()).isEqualTo(genreNames);
		assertThat(recommendationConfiguration.getBody().getUserId()).isEqualTo(loggedUser.getUserId());
	}

	@Test
	void should_create_recommendation_configuration_and_recommend_movies() {
		// given
		final LoggedUser loggedUser = loggedUserProvider.getLoggedUser();
		final List<String> genreNames = List.of("WAR");

		// when
		final ResponseEntity<Long> createRecommendationResponse = createRecommendationConfigurationRest.createRecommendationConfiguration(
				loggedUser, CreateRecommendationConfigurationDto.builder().limitToGenres(genreNames).build());

		// then
		assertThat(createRecommendationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		simulateRecommendationsAvailable();
		final ResponseEntity<RecommendedMoviesDto> latestRecommendedMoviesResponse = getLatestRecommendedMoviesRest.getLatestRecommendedMovies(loggedUser);

		// then
		assertThat(latestRecommendedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(latestRecommendedMoviesResponse.getBody().getMovieInfos().size()).isGreaterThan(0);
		assertThat(latestRecommendedMoviesResponse.getBody().getMovieInfos())
				.extracting(MovieInfoDto::getGenresNames).allMatch(movieGenres -> movieGenres.containsAll(genreNames));
	}

	@Test
	void should_update_recommendation_configuration_and_recommend_movies() {
		// given
		final LoggedUser loggedUser = loggedUserProvider.getLoggedUser();
		final List<String> genreNames = List.of("WAR");
		final List<String> updatedGenreNames = List.of("THRILLER");

		// when
		final ResponseEntity<Long> createRecommendationResponse = createRecommendationConfigurationRest.createRecommendationConfiguration(
				loggedUser, CreateRecommendationConfigurationDto.builder().limitToGenres(genreNames).build());

		// then
		assertThat(createRecommendationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<Void> updateRecommendationConfigurationResponse = updateRecommendationConfigurationRest
				.updateRecommendationConfiguration(loggedUser, UpdateRecommendationConfigurationDto.builder()
						.limitToGenres(updatedGenreNames)
						.build());

		// then
		assertThat(updateRecommendationConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// when
		final ResponseEntity<RecommendationConfigurationDto> getRecommendationConfigurationResponse =
				getRecommendationConfigurationRest.getRecommendationConfiguration(loggedUser);

		// then
		assertThat(getRecommendationConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getRecommendationConfigurationResponse.getBody().getGenreNames()).isEqualTo(updatedGenreNames);

		// when
		simulateRecommendationsAvailable();
		final ResponseEntity<RecommendedMoviesDto> getLatestRecommendedMoviesResponse =
				getLatestRecommendedMoviesRest.getLatestRecommendedMovies(loggedUser);

		assertThat(getLatestRecommendedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getLatestRecommendedMoviesResponse.getBody().getMovieInfos()).hasSize(2);
		assertThat(getLatestRecommendedMoviesResponse.getBody().getMovieInfos())
				.extracting(MovieInfoDto::getGenresNames).allMatch(movieGenres -> movieGenres.containsAll(updatedGenreNames));
	}

	@Test
	void should_forbid_endpoints_when_not_logged_in_user() {
		// given
		final Integer userId = createAnyUserId().getValue();
		final LoggedUser loggedUser = new LoggedUser(userId, List.of("asd"));

		// when
		final ResponseEntity<MovieGenresDto> getMovieGenresResponse = getMovieGenresRest.getMovieGenres(loggedUser);

		final ResponseEntity<Long> createRecommendationResponse = createRecommendationConfigurationRest.createRecommendationConfiguration(
				loggedUser, CreateRecommendationConfigurationDto.builder().limitToGenres(List.of("WAR")).build());

		final ResponseEntity<RecommendationConfigurationDto> recommendationConfiguration =
				getRecommendationConfigurationRest.getRecommendationConfiguration(loggedUser);

		final ResponseEntity<RecommendedMoviesDto> latestRecommendedMoviesResponse = getLatestRecommendedMoviesRest.getLatestRecommendedMovies(loggedUser);

		final ResponseEntity<Void> updateRecommendationConfigurationResponse = updateRecommendationConfigurationRest
				.updateRecommendationConfiguration(loggedUser, UpdateRecommendationConfigurationDto.builder()
						.limitToGenres(List.of("ACTION"))
						.build());

		// then
		assertThat(getMovieGenresResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(createRecommendationResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(recommendationConfiguration.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(latestRecommendedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(updateRecommendationConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	private void simulateRecommendationsAvailable() {
		recommendationDecisionHandler.recommendMovies();
	}

}

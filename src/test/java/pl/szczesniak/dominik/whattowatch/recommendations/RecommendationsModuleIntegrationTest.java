package pl.szczesniak.dominik.whattowatch.recommendations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.GetRecommendationConfigurationInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.GetRecommendationConfigurationInvoker.RecommendationConfigurationDto;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations.CreateRecommendationConfigurationInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations.CreateRecommendationConfigurationInvoker.CreateRecommendationConfigurationDto;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations.GetMovieGenresInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations.UpdateRecommendationConfigurationInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies.GetLatestRecommendedMoviesInvoker;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.scheduler.RecommendationDecisionHandler;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUserSample;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations.GetMovieGenresInvoker.MovieGenresDto;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.configurations.UpdateRecommendationConfigurationInvoker.UpdateRecommendationConfigurationDto;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies.GetLatestRecommendedMoviesInvoker.MovieInfoDto;
import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendedmovies.GetLatestRecommendedMoviesInvoker.RecommendedMoviesDto;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RecommendationsModuleIntegrationTest {

	@Autowired
	private RecommendationDecisionHandler recommendationDecisionHandler;

	@Autowired
	private UserService userService;

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

	private Integer userId;

	@BeforeEach
	void setUp() {
		userId = userService.createUser(CreateUserSample.builder().build()).getValue();
	}

	@Test
	void should_get_genres_and_create_recommendation_configuration_and_recommend_movies() {
		// when
		final ResponseEntity<MovieGenresDto> getMovieGenresResponse = getMovieGenresRest.getMovieGenres();

		// then
		assertThat(getMovieGenresResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getMovieGenresResponse.getBody().getGenresNames()).contains("WAR");

		// given
		final List<String> genreNames = List.of("WAR");

		// when
		final ResponseEntity<Long> createRecommendationResponse = createRecommendationConfigurationRest.createRecommendationConfiguration(
				userId, CreateRecommendationConfigurationDto.builder().genres(genreNames).build());

		// then
		assertThat(createRecommendationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<RecommendationConfigurationDto> recommendationConfiguration =
				getRecommendationConfigurationRest.getRecommendationConfiguration(userId);

		// then
		assertThat(recommendationConfiguration.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(recommendationConfiguration.getBody().getConfigurationId()).isEqualTo(createRecommendationResponse.getBody());
		assertThat(recommendationConfiguration.getBody().getGenreNames()).isEqualTo(genreNames);
		assertThat(recommendationConfiguration.getBody().getUserId()).isEqualTo(userId);

		// when
		recommendationDecisionHandler.recommendMovies();
		final ResponseEntity<RecommendedMoviesDto> latestRecommendedMoviesResponse = getLatestRecommendedMoviesRest.getLatestRecommendedMovies(userId);

		// then
		assertThat(latestRecommendedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(latestRecommendedMoviesResponse.getBody().getMovieInfos())
				.extracting(MovieInfoDto::getGenresNames).allMatch(movieGenres -> movieGenres.containsAll(genreNames));
	}

	@Test
	void should_update_recommendation_configuration_and_recommend_movies() {
		// given
		final List<String> genreNames = List.of("WAR");
		final List<String> updatedGenreNames = List.of("THRILLER");

		// when
		final ResponseEntity<Long> createRecommendationResponse = createRecommendationConfigurationRest.createRecommendationConfiguration(
				userId, CreateRecommendationConfigurationDto.builder().genres(genreNames).build());

		// then
		assertThat(createRecommendationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<Void> updateRecommendationConfigurationResponse = updateRecommendationConfigurationRest
				.updateRecommendationConfiguration(userId, UpdateRecommendationConfigurationDto.builder()
						.genres(updatedGenreNames)
						.build());

		// then
		assertThat(updateRecommendationConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// when
		final ResponseEntity<RecommendationConfigurationDto> getRecommendationConfigurationResponse =
				getRecommendationConfigurationRest.getRecommendationConfiguration(userId);

		// then
		assertThat(getRecommendationConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getRecommendationConfigurationResponse.getBody().getGenreNames()).isEqualTo(updatedGenreNames);

		// when
		recommendationDecisionHandler.recommendMovies();
		final ResponseEntity<RecommendedMoviesDto> getLatestRecommendedMoviesResponse =
				getLatestRecommendedMoviesRest.getLatestRecommendedMovies(userId);

		assertThat(getLatestRecommendedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getLatestRecommendedMoviesResponse.getBody().getMovieInfos())
				.extracting(MovieInfoDto::getGenresNames).allMatch(movieGenres -> movieGenres.containsAll(updatedGenreNames));
	}

}

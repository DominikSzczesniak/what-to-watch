package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.Clock;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.recommendations.domain.TestRecommendationServiceConfiguration.recommendationFacade;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendationsFacadeConfigurationsTest {

	private RecommendationFacade tut;

	@BeforeEach
	void setUp() {
		tut = recommendationFacade(Clock.systemDefaultZone());
	}

	@Test
	void should_create_and_find_recommendation_configuration() {
		// given
		final UserId userId = createAnyUserId();

		// when
		final ConfigurationId configurationId = tut.create(CreateRecommendationConfigurationSample.builder().userId(userId).build());
		final RecommendationConfigurationQueryResult configuration = tut.getLatestRecommendationConfiguration(userId);

		// then
		assertThat(configuration.getUserId()).isEqualTo(userId.getValue());
		assertThat(configuration.getConfigurationId()).isEqualTo(configurationId.getValue());
	}

	@Test
	void should_find_updated_recommendation_configuration() {
		// given
		final UserId userId = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(userId)
				.genreNames(Set.of(MovieGenre.ADVENTURE))
				.build());

		final RecommendationConfigurationQueryResult configuration = tut.getLatestRecommendationConfiguration(userId);
		tut.update(UpdateRecommendationConfigurationSample.builder().genreNames(Set.of(MovieGenre.ACTION)).userId(userId).build());

		// when
		final RecommendationConfigurationQueryResult updatedConfiguration = tut.getLatestRecommendationConfiguration(userId);

		// then
		assertThat(updatedConfiguration).isEqualTo(configuration);
		assertThat(updatedConfiguration.getGenres()).containsExactly(MovieGenre.ACTION);
	}

	@Test
	void should_not_be_able_to_update_non_existing_recommendation_configuration() {
		// given
		final UserId userId = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.update(UpdateRecommendationConfigurationSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_user_recommendation_configuration_not_found() {
		// given
		final UserId userId = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.getLatestRecommendationConfiguration(userId));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_find_all_users_with_recommendation_configurations() {
		// given
		final UserId userId1 = createAnyUserId();
		final UserId userId2 = createAnyUserId();
		final UserId userId3 = createAnyUserId();
		final UserId userId4 = createAnyUserId();

		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId1).build());
		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId2).build());
		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId4).build());

		// when
		final List<UserId> allUsersWithRecommendationConfiguration = tut.findAllUsersWithRecommendationConfiguration();

		// then
		assertThat(allUsersWithRecommendationConfiguration).hasSize(3);
	}

}
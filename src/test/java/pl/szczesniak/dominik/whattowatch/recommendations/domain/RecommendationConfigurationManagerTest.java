package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendationConfigurationManagerTest {

	private RecommendationConfigurationManager tut;

	@BeforeEach
	void setUp() {
		tut = new RecommendationConfigurationManager(new InMemoryRecommendationConfigurationRepository());
	}

	@Test
	void should_not_be_able_to_create_configuration_when_already_exists() {
		// given
		final UserId userId = createAnyUserId();

		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId).build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.create(CreateRecommendationConfigurationSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectAlreadyExistsException.class);
	}

	@Test
	void should_not_be_able_to_update_non_existing_configuration() {
		// given
		final UserId userId = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.update(UpdateRecommendationConfigurationSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_find_updated_recommendation_configuration() {
		// given
		final UserId userId = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(userId)
				.genreNames(Set.of(MovieGenre.ADVENTURE))
				.build());
		final RecommendationConfiguration configuration = tut.findBy(userId);
		tut.update(UpdateRecommendationConfigurationSample.builder().userId(userId).build());

		// when
		final RecommendationConfiguration updatedConfiguration = tut.findBy(userId);

		// then
		assertThat(updatedConfiguration).isEqualTo(configuration);
	}

	@Test
	void should_find_user_recommendation_configuration() {
		// given
		final UserId userId = createAnyUserId();
		final ConfigurationId configurationId = tut.create(CreateRecommendationConfigurationSample.builder().userId(userId).build());

		// when
		final RecommendationConfiguration foundConfiguration = tut.findBy(userId);

		// then
		assertThat(foundConfiguration.getConfigurationId()).isEqualTo(configurationId);
	}

	@Test
	void should_throw_exception_when_user_configuration_not_found() {
		// given
		final UserId userId = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.findBy(userId));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_find_all_recommendation_configurations() {
		// given
		final UserId userId1 = createAnyUserId();
		final UserId userId2 = createAnyUserId();
		final UserId userId3 = createAnyUserId();
		final UserId userId4 = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId1).build());
		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId2).build());
		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId3).build());
		tut.create(CreateRecommendationConfigurationSample.builder().userId(userId4).build());

		// when
		final List<RecommendationConfiguration> foundConfigs = tut.findAll();

		// then
		assertThat(foundConfigs).hasSize(4);
	}

}
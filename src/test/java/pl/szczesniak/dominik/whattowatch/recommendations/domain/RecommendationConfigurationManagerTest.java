package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateConfigurationSample;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendationConfigurationManagerTest {

	private RecommendationConfigurationManager tut;

	@BeforeEach
	void setUp() {
		tut = new RecommendationConfigurationManager(new InMemoryRecommendationConfigurationRepository());
	}

	@Test
	void should_find_updated_recommendation_configuration() {
		// given
		final UserId userId = createAnyUserId();
		tut.create(CreateConfigurationSample.builder().userId(userId).genreNames(Set.of(MovieGenre.ADVENTURE)).build());
		final RecommendationConfiguration configuration = tut.findBy(userId);

		// when
		tut.updateRecommendationConfiguration(new UpdateRecommendationConfiguration(Set.of(MovieGenre.TV_MOVIE), userId));

		// then
		assertThat(tut.findBy(userId)).isEqualTo(configuration);
	}

}
package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.RecommendMoviesSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.recommendations.domain.TestRecommendationFacadeConfiguration.recommendationFacade;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendationFacadeTest {

	private RecommendationFacade tut;

	@BeforeEach
	void setUp() {
		tut = recommendationFacade(new FakeClock());
	}

	@Test
	void should_recommend_popular_movies() {
		// when
		final MovieInfoResponse movieInfoResponse = tut.recommendPopularMovies();

		// then
		assertThat(movieInfoResponse.getResults()).hasSizeGreaterThan(0);
	}

	@Test
	void should_throw_exception_when_no_recommendation_configuration_found() {
		// given
		final UserId user = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.recommendMovies(RecommendMoviesSample.builder().userId(user).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_no_recommended_movies_found() {
		// given
		final UserId user = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.getLatestRecommendedMovies(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

}
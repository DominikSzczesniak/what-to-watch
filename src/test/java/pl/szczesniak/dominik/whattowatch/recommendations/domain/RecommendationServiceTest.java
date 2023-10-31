package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.recommendations.domain.TestRecommendationServiceConfiguration.recommendationService;

class RecommendationServiceTest {

	private RecommendationService tut;
	private RecommendationConfigurationManager configManager;

	@BeforeEach
	void setUp() {
		configManager = new RecommendationConfigurationManager(new InMemoryRecommendationConfigurationRepository());
		tut = recommendationService(configManager);
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
		final UserId user = new UserId(1);

		// when
		final Throwable thrown = catchThrowable(() -> tut.recommendMoviesByConfiguration(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_recommend_two_movies_based_on_user_configuration() {
		// given
		final UserId user1 = new UserId(1);
		final UserId user2 = new UserId(2);
		configManager.create(user1, Set.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE));
		configManager.create(user2, Set.of(MovieGenre.ADVENTURE));

		// when
		final RecommendedMovies recommendedMoviesUser1 = tut.recommendMoviesByConfiguration(user1);
		final RecommendedMovies recommendedMoviesUser2 = tut.recommendMoviesByConfiguration(user2);

		// then
		assertThat(recommendedMoviesUser1.getMovies()).hasSize(2);
		final boolean genresMatchUser1 = recommendedMoviesUser1.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ACTION) && genres.contains(MovieGenre.TV_MOVIE));
		assertThat(genresMatchUser1).isTrue();

		assertThat(recommendedMoviesUser2.getMovies()).hasSize(2);
		final boolean genresMatchUser2 = recommendedMoviesUser2.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ADVENTURE));
		assertThat(genresMatchUser2).isTrue();
	}

	@Test
	void should_recommend_one_movie_when_only_one_found_from_database() {
		// given
		final UserId user = new UserId(1);
		configManager.create(user, Set.of(MovieGenre.ROMANCE));

		// when
		final RecommendedMovies recommendedMovies = tut.recommendMoviesByConfiguration(user);

		// then
		assertThat(recommendedMovies.getMovies()).hasSize(1);
	}

	@Test
	void should_not_recommend_same_movies_twice_in_a_row() throws InterruptedException {
		// given
		final UserId user = new UserId(1);
		configManager.create(user, Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE));

		// when
		final RecommendedMovies recommendedMovies1 = tut.recommendMoviesByConfiguration(user);
		Thread.sleep(1000);
		final RecommendedMovies recommendedMovies2 = tut.recommendMoviesByConfiguration(user);
		final RecommendedMovies recommendedMovies3 = tut.recommendMoviesByConfiguration(user);

		// then
		assertThat(recommendedMovies1.getMovies()).isNotEqualTo(recommendedMovies2.getMovies());
		assertThat(recommendedMovies1.getMovies()).isEqualTo(recommendedMovies3.getMovies());
	}

	@Test
	void should_find_latest_recommended_movies() {
		// given
		final UserId user = new UserId(1);
		configManager.create(user, Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE));

		// when
		final RecommendedMovies recommendedMovies = tut.recommendMoviesByConfiguration(user);

		final RecommendedMovies latestRecommendedMovies = tut.findLatestRecommendedMovies(user);

		// then
		assertThat(latestRecommendedMovies).isEqualTo(recommendedMovies);
	}

	@Test
	void should_throw_exception_when_no_recommended_movies_found() {
		// given
		final UserId user = new UserId(1);
		configManager.create(user, Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE));

		// when
		final Throwable thrown = catchThrowable(() -> tut.findLatestRecommendedMovies(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

}
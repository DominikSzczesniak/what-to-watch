package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.recommendations.domain.TestRecommendationServiceConfiguration.recommendationService;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendationServiceTest {

	private RecommendationService tut;
	private RecommendationConfigurationManager configManager;

	private static final int EXPECTED_RECOMMENDED_MOVIES_COUNT = 2;

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
		final UserId user = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.recommendMoviesByConfiguration(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_find_latest_recommended_movies() {
		// given
		final UserId user = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		final RecommendedMoviesId previouslyRecommendedMoviesId = tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesId latestRecommendedMoviesId = tut.recommendMoviesByConfiguration(user);

		// when
		final RecommendedMovies latestRecommendedMovies = tut.findLatestRecommendedMovies(user);

		// then
		assertThat(latestRecommendedMovies.getRecommendedMoviesId()).isNotEqualTo(previouslyRecommendedMoviesId);
		assertThat(latestRecommendedMovies.getRecommendedMoviesId()).isEqualTo(latestRecommendedMoviesId);
	}

	@Test
	void should_recommend_two_movies_based_on_user_recommendation_configuration() {
		// given
		final UserId user1 = createAnyUserId();
		final UserId user2 = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user1)
				.genreNames(Set.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE))
				.build());
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user2)
				.genreNames(Set.of(MovieGenre.ADVENTURE))
				.build());

		// when
		final RecommendedMoviesId recommendedMoviesIdUser1 = tut.recommendMoviesByConfiguration(user1);
		final RecommendedMovies recommendedMoviesUser1 = tut.findLatestRecommendedMovies(user1);

		final RecommendedMoviesId recommendedMoviesIdUser2 = tut.recommendMoviesByConfiguration(user2);
		final RecommendedMovies recommendedMoviesUser2 = tut.findLatestRecommendedMovies(user2);

		// then
		assertThat(recommendedMoviesUser1.getRecommendedMoviesId()).isEqualTo(recommendedMoviesIdUser1);
		assertThat(recommendedMoviesUser1.getMovies()).hasSize(EXPECTED_RECOMMENDED_MOVIES_COUNT);
		final boolean genresMatchUser1 = recommendedMoviesUser1.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ACTION) && genres.contains(MovieGenre.TV_MOVIE));
		assertThat(genresMatchUser1).isTrue();

		assertThat(recommendedMoviesUser2.getRecommendedMoviesId()).isEqualTo(recommendedMoviesIdUser2);
		assertThat(recommendedMoviesUser2.getMovies()).hasSize(EXPECTED_RECOMMENDED_MOVIES_COUNT);
		final boolean genresMatchUser2 = recommendedMoviesUser2.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ADVENTURE));
		assertThat(genresMatchUser2).isTrue();
	}

	@Test
	void should_recommend_one_movie_when_only_one_found_in_database() {
		// given
		final UserId user = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.ROMANCE))
				.build());

		tut.recommendMoviesByConfiguration(user);

		// when
		final RecommendedMovies recommendedMovies = tut.findLatestRecommendedMovies(user);

		// then
		assertThat(recommendedMovies.getMovies()).hasSize(1);
	}

	@Test
	void should_not_recommend_same_movies_twice_in_a_row() {
		// given
		final UserId user = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		// when
		tut.recommendMoviesByConfiguration(user);
		final RecommendedMovies recommendedMovies1 = tut.findLatestRecommendedMovies(user);

		tut.recommendMoviesByConfiguration(user);
		final RecommendedMovies recommendedMovies2 = tut.findLatestRecommendedMovies(user);

		tut.recommendMoviesByConfiguration(user);
		final RecommendedMovies recommendedMovies3 = tut.findLatestRecommendedMovies(user);

		// then
		assertThat(recommendedMovies2.getMovies()).doesNotContainAnyElementsOf(recommendedMovies1.getMovies());
		assertThat(recommendedMovies3.getMovies()).doesNotContainAnyElementsOf(recommendedMovies2.getMovies());
	}

	@Test
	void should_throw_exception_when_no_recommended_movies_found() {
		// given
		final UserId user = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.findLatestRecommendedMovies(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_find_recommended_movies_for_current_interval() {
		// given
		final UserId user = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		tut.recommendMoviesByConfiguration(user);

		// when
		boolean hasRecommendedMoviesForCurrentInterval = tut.hasRecommendedMoviesForCurrentInterval(user);

		// then
		assertThat(hasRecommendedMoviesForCurrentInterval).isTrue();
	}

	@Test
	void should_not_find_recommended_movies_for_current_interval() {
		// given
		final UserId user = createAnyUserId();
		configManager.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		// when
		boolean hasRecommendedMoviesForCurrentInterval = tut.hasRecommendedMoviesForCurrentInterval(user);

		// then
		assertThat(hasRecommendedMoviesForCurrentInterval).isFalse();
	}

}
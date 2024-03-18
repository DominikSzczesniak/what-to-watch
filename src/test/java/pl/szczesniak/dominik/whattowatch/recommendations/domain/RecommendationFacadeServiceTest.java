package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfigurationSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.recommendations.domain.TestRecommendationServiceConfiguration.recommendationFacade;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendationFacadeServiceTest {

	private RecommendationFacade tut;

	private static final int EXPECTED_RECOMMENDED_MOVIES_COUNT = 2;

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
		final Throwable thrown = catchThrowable(() -> tut.recommendMoviesByConfiguration(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_find_latest_recommended_movies() {
		// given
		final UserId user = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult latestRecommendedMovies1 = tut.getLatestRecommendedMovies(user);
		FakeClock.simulateWeeksIntoFuture(1);

		// when
		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult latestRecommendedMovies2 = tut.getLatestRecommendedMovies(user);

		// then
		assertThat(latestRecommendedMovies2.getRecommendedMoviesId()).isNotEqualTo(latestRecommendedMovies1.getRecommendedMoviesId());
		assertThat(latestRecommendedMovies2.getCreationDate()).isAfterOrEqualTo(latestRecommendedMovies1.getCreationDate());
	}

	@Test
	void should_recommend_two_movies_based_on_user_recommendation_configuration() {
		// given
		final UserId user1 = createAnyUserId();
		final UserId user2 = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user1)
				.genreNames(Set.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE))
				.build());
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user2)
				.genreNames(Set.of(MovieGenre.ADVENTURE))
				.build());

		// when
		tut.recommendMoviesByConfiguration(user1);
		final RecommendedMoviesQueryResult recommendedMoviesUser1 = tut.getLatestRecommendedMovies(user1);

		tut.recommendMoviesByConfiguration(user2);
		final RecommendedMoviesQueryResult recommendedMoviesUser2 = tut.getLatestRecommendedMovies(user2);

		// then
		assertThat(recommendedMoviesUser1.getMovies()).hasSize(EXPECTED_RECOMMENDED_MOVIES_COUNT);
		final boolean genresMatchUser1 = recommendedMoviesUser1.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ACTION) && genres.contains(MovieGenre.TV_MOVIE));
		assertThat(genresMatchUser1).isTrue();

		assertThat(recommendedMoviesUser2.getMovies()).hasSize(EXPECTED_RECOMMENDED_MOVIES_COUNT);
		final boolean genresMatchUser2 = recommendedMoviesUser2.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ADVENTURE));
		assertThat(genresMatchUser2).isTrue();
	}

	@Test
	void should_recommend_movies_when_no_genres_limitation() {
		// given
		final UserId user = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Collections.emptySet())
				.build());

		// when
		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult recommendedMovies = tut.getLatestRecommendedMovies(user);

		// then
		assertThat(recommendedMovies.getMovies())
				.flatExtracting(MovieInfo::getGenres)
				.allSatisfy(genre -> assertThat(MovieGenre.allValues()).contains(genre));
	}

	@Test
	void should_recommend_one_movie_when_only_one_found_in_database() {
		// given
		final UserId user = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.ROMANCE))
				.build());

		tut.recommendMoviesByConfiguration(user);

		// when
		final RecommendedMoviesQueryResult recommendedMovies = tut.getLatestRecommendedMovies(user);

		// then
		assertThat(recommendedMovies.getMovies()).hasSize(1);
	}

	@Test
	void should_not_recommend_same_movies_twice_in_a_row() {
		// given
		final UserId user = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		// when
		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult recommendedMovies1 = tut.getLatestRecommendedMovies(user);
		FakeClock.simulateWeeksIntoFuture(1);

		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult recommendedMovies2 = tut.getLatestRecommendedMovies(user);
		FakeClock.simulateWeeksIntoFuture(1);

		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult recommendedMovies3 = tut.getLatestRecommendedMovies(user);

		// then
		assertThat(recommendedMovies2.getMovies()).doesNotContainAnyElementsOf(recommendedMovies1.getMovies());
		assertThat(recommendedMovies3.getMovies()).doesNotContainAnyElementsOf(recommendedMovies2.getMovies());
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

	@Test
	void should_not_recommend_when_already_recommended_movies_in_current_interval() {
		// given
		final UserId user = createAnyUserId();
		tut.create(CreateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE))
				.build());

		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult recommendedMovies = tut.getLatestRecommendedMovies(user);

		final boolean genresMatch = recommendedMovies.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.FANTASY) && genres.contains(MovieGenre.ADVENTURE));
		assertThat(genresMatch).isTrue();

		tut.update(UpdateRecommendationConfigurationSample.builder()
				.userId(user)
				.genreNames(Set.of(MovieGenre.ACTION, MovieGenre.WAR))
				.build());

		// when
		tut.recommendMoviesByConfiguration(user);
		final RecommendedMoviesQueryResult latestRecommendedMovies = tut.getLatestRecommendedMovies(user);

		// then
		assertThat(latestRecommendedMovies.getRecommendedMoviesId()).isEqualTo(recommendedMovies.getRecommendedMoviesId());
	}

	static class FakeClock extends Clock {

		@Setter
		static Instant fixedTime = null;

		static public void simulateWeeksIntoFuture(long weeks) {
			fixedTime = Optional.ofNullable(fixedTime).orElseGet(Instant::now).plusSeconds(weeks * 7 * 24 * 60 * 60);
		}

		@Override
		public ZoneId getZone() {
			return ZoneId.systemDefault();
		}

		@Override
		public Clock withZone(final ZoneId zone) {
			return new FakeClock();
		}

		@Override
		public Instant instant() {
			return ofNullable(fixedTime).orElseGet(Instant::now);
		}
	}

}
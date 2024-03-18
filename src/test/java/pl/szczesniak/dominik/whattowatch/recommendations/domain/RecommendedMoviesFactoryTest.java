package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateMovieInfoSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class RecommendedMoviesFactoryTest {

	private RecommendedMoviesFactory tut;

	@BeforeEach
	void setUp() {
		tut = new RecommendedMoviesFactory(2);
	}

	@Test
	void should_recommend_one_movie_when_only_one_found_in_database() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Set.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE);

		final List<MovieInfo> fromApi = List.of(CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE)).build());

		// when
		final RecommendedMovies newRecommendedMovies = tut.createNewRecommendedMovies(genres, fromApi, emptyList(), userId);

		// then
		assertThat(newRecommendedMovies.getMovies()).hasSize(1);
	}

	@Test
	void should_recommend_movies_when_no_genre_limits() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Collections.emptySet();

		final List<MovieInfo> fromApi = List.of(
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.CRIME)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.WAR, MovieGenre.FANTASY)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.COMEDY)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.ACTION)).build()
		);

		// when
		final RecommendedMovies recommendedMovies = tut.createNewRecommendedMovies(genres, fromApi, emptyList(), userId);

		// then
		assertThat(recommendedMovies.getMovies().size()).isEqualTo(2);
	}

	@Test
	void should_throw_exception_when_null_genres() {
		// given
		final UserId userId = createAnyUserId();

		final List<MovieInfo> fromApi = List.of(
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.CRIME)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.WAR, MovieGenre.FANTASY)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.COMEDY)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.ACTION)).build()
		);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createNewRecommendedMovies(null, fromApi, emptyList(), userId));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_null_movies_from_api() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Set.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createNewRecommendedMovies(genres, null, emptyList(), userId));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_only_movie_possible_to_recommend_is_the_same_as_previously_recommended_movie() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Set.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE);

		final MovieInfo movie = CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE)).build();
		final List<MovieInfo> previouslyRecommended = List.of(movie);
		final List<MovieInfo> fromApi = List.of(movie);

		// when
		final Throwable thrown = catchThrowable(() -> tut.createNewRecommendedMovies(genres, fromApi, previouslyRecommended, userId));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_no_movies_provided_from_database() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Set.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE);

		final List<MovieInfo> fromApi = emptyList();

		// when
		final Throwable thrown = catchThrowable(() -> tut.createNewRecommendedMovies(genres, fromApi, emptyList(), userId));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_recommend_movies() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Set.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE);

		final MovieInfo expectedMovieToRecommend1 = CreateMovieInfoSample.builder()
				.genres(List.of(MovieGenre.ADVENTURE, MovieGenre.TV_MOVIE, MovieGenre.ACTION)).build();
		final MovieInfo expectedMovieToRecommend2 = CreateMovieInfoSample.builder()
				.genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE)).build();

		final List<MovieInfo> fromApi = List.of(
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.CRIME)).build(),
				expectedMovieToRecommend1,
				expectedMovieToRecommend2,
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.ACTION)).build()
		);


		// when
		final RecommendedMovies newRecommendedMovies = tut.createNewRecommendedMovies(genres, fromApi, emptyList(), userId);

		// then
		assertThat(newRecommendedMovies.getMovies()).containsExactlyInAnyOrder(expectedMovieToRecommend1, expectedMovieToRecommend2);
	}

	@Test
	void should_not_recommend_same_movies_twice() {
		// given
		final UserId userId = createAnyUserId();
		final Set<MovieGenre> genres = Set.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE);

		final MovieInfo previouslyRecommendedMovie1 = CreateMovieInfoSample.builder()
				.genres(List.of(MovieGenre.ADVENTURE, MovieGenre.TV_MOVIE, MovieGenre.ACTION)).build();
		final MovieInfo previouslyRecommendedMovie2 = CreateMovieInfoSample.builder()
				.genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.ADVENTURE)).build();

		final List<MovieInfo> previouslyRecommendedMovies = List.of(previouslyRecommendedMovie1, previouslyRecommendedMovie2);

		final List<MovieInfo> recommendedFromApi = List.of(
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE)).build(),
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.TV_MOVIE, MovieGenre.CRIME)).build(),
				previouslyRecommendedMovie2,
				previouslyRecommendedMovie1,
				CreateMovieInfoSample.builder().genres(List.of(MovieGenre.ACTION)).build()
		);

		// when
		final RecommendedMovies newRecommendedMovies = tut.createNewRecommendedMovies(genres,
				recommendedFromApi,
				previouslyRecommendedMovies,
				userId
		);

		// then
		assertThat(newRecommendedMovies.getMovies()).doesNotContain(previouslyRecommendedMovie1, previouslyRecommendedMovie2);
		assertThat(newRecommendedMovies.getMovies()).extracting(MovieInfo::getGenres)
				.containsAnyOf(List.of(MovieGenre.TV_MOVIE), List.of(MovieGenre.ADVENTURE));
	}

}
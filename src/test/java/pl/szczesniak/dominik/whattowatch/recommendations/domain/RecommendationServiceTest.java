package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoSample;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecommendationServiceTest {

	private RecommendationService tut;
	private RecommendationConfigurationManager configManager;
	private MovieInfoApi movieInfoApi;

	@BeforeEach
	void setUp() {
		configManager = new RecommendationConfigurationManager(new InMemoryRecommendationConfigurationRepository());
		movieInfoApi = mock(MovieInfoApi.class);
		tut = new RecommendationService(configManager, movieInfoApi);
	}

	@Test
	void should_recommend_popular_movies() {
		// given
		when(movieInfoApi.getPopularMovies()).thenReturn(new MovieInfoResponse(Collections.emptyList()));

		// when
		final MovieInfoResponse movieInfoResponse = tut.recommendPopularMovies();

		// then
		assertThat(movieInfoResponse.getResults()).hasSize(0);
	}

	@Test
	void should_throw_exception_when_no_recommendation_configuration_found() {
		// given
		final UserId user = new UserId(1);

		// when
		Throwable thrown = catchThrowable(() -> tut.recommendMoviesByConfiguration(user));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_recommend_two_movies() { // TODO: fix, sometimes passess, sometimes npe, sometimes false
		// given
		final UserId user = new UserId(1);
		configManager.create(user, Set.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE));
		when(movieInfoApi.getGenres()).thenReturn(genresMap());
		final MovieInfoResponse movieInfoResponse = new MovieInfoResponse(List.of(
				MovieInfoSample.builder().genres(List.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE)).build(),
				MovieInfoSample.builder().genres(List.of(MovieGenre.ACTION, MovieGenre.CRIME)).build(),
				MovieInfoSample.builder().genres(List.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE, MovieGenre.ANIMATION)).build(),
				MovieInfoSample.builder().genres(List.of(MovieGenre.ACTION, MovieGenre.ADVENTURE, MovieGenre.COMEDY, MovieGenre.CRIME)).build()
		));
		when(movieInfoApi.getMoviesByGenre(List.of(28L, 10770L))).thenReturn(movieInfoResponse); // any + verify moze api in memory

		// when
		final RecommendedMovies recommendedMovies = tut.recommendMoviesByConfiguration(user);

		// then
		assertThat(recommendedMovies.getMovies()).hasSize(2);

		final boolean genresMatch = recommendedMovies.getMovies().stream()
				.map(MovieInfo::getGenres)
				.allMatch(genres -> genres.contains(MovieGenre.ACTION) && genres.contains(MovieGenre.TV_MOVIE));
		assertThat(genresMatch).isTrue();
	}

	@Test
	void should_recommend_one_movie_when_only_one_found_from_database() {
		// given
		final UserId user = new UserId(1);
		configManager.create(user, Set.of(MovieGenre.ADVENTURE));
		when(movieInfoApi.getGenres()).thenReturn(genresMap());
		final MovieInfoResponse movieInfoResponse = new MovieInfoResponse(List.of(
				MovieInfoSample.builder().genres(List.of(MovieGenre.ACTION, MovieGenre.ADVENTURE, MovieGenre.COMEDY, MovieGenre.CRIME)).build()
		));
		when(movieInfoApi.getMoviesByGenre(List.of(12L))).thenReturn(movieInfoResponse);

		// when
		final RecommendedMovies recommendedMovies = tut.recommendMoviesByConfiguration(user);

		// then
		assertThat(recommendedMovies.getMovies()).hasSize(1);
	}

	private MovieGenreResponse genresMap() {
		final Map<Long, MovieGenre> assignedGenreIds = new HashMap<>();
		assignedGenreIds.put(28L, MovieGenre.ACTION);
		assignedGenreIds.put(12L, MovieGenre.ADVENTURE);
		assignedGenreIds.put(16L, MovieGenre.ANIMATION);
		assignedGenreIds.put(35L, MovieGenre.COMEDY);
		assignedGenreIds.put(80L, MovieGenre.CRIME);
		assignedGenreIds.put(99L, MovieGenre.DOCUMENTARY);
		assignedGenreIds.put(18L, MovieGenre.DRAMA);
		assignedGenreIds.put(10751L, MovieGenre.FAMILY);
		assignedGenreIds.put(14L, MovieGenre.FANTASY);
		assignedGenreIds.put(36L, MovieGenre.HISTORY);
		assignedGenreIds.put(27L, MovieGenre.HORROR);
		assignedGenreIds.put(10402L, MovieGenre.MUSIC);
		assignedGenreIds.put(9648L, MovieGenre.MYSTERY);
		assignedGenreIds.put(10749L, MovieGenre.ROMANCE);
		assignedGenreIds.put(878L, MovieGenre.SCIENCE_FICTION);
		assignedGenreIds.put(10770L, MovieGenre.TV_MOVIE);
		assignedGenreIds.put(53L, MovieGenre.THRILLER);
		assignedGenreIds.put(10752L, MovieGenre.WAR);
		assignedGenreIds.put(37L, MovieGenre.WESTERN);
		return new MovieGenreResponse(assignedGenreIds);
	}
}
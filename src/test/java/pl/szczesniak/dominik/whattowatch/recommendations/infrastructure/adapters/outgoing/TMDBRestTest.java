package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import org.junit.jupiter.api.BeforeEach;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TMDBRestTest {

	private MovieInfoApi tut;

	@BeforeEach
	void setUp() {
		tut = new TMDBMovieInfoApi("4663542b69490ce1434ece35ebad7665", "https://api.themoviedb.org/3");
	}

	//	@Test
	// Only for manual testing - I don't want to break building an app with maven when TMDB fails.
	void should_get_popular_movies() {
		// when
		final MovieInfoResponse popularMovies = tut.getPopularMovies();

		// then
		assertThat(popularMovies.getResults()).hasSizeGreaterThan(0);
	}

	//	@Test
	void should_get_movie_genres() {
		// when
		final MovieGenreResponse genres = tut.getGenres();

		// then
		assertThat(genres.getGenres()).hasSizeGreaterThan(0);
	}

	//	@Test
	void should_find_movies_by_genre_id() {
		// given
		final Map<Long, MovieGenre> allGenres = tut.getGenres().getGenres();
		final Set<MovieGenre> genres = new HashSet<>(allGenres.values());

		// when
		final MovieInfoResponse moviesByGenre = tut.getMoviesByGenre(genres);

		// then
		assertThat(moviesByGenre.getResults()).isNotNull();
		assertThat(moviesByGenre.getResults()).hasSizeGreaterThan(0);
	}

}
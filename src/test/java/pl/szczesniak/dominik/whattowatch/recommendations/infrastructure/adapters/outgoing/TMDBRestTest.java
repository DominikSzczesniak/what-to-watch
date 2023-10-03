package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TMDBRestTest {

	private MovieInfoApi tut;

	@BeforeEach
	void setUp() {
		tut = new TMDBMovieInfoApi("4663542b69490ce1434ece35ebad7665", "https://api.themoviedb.org/3");
	}

	@Test
	void should_get_popular_movies() {
		// when
		final MovieInfoResponse popularMovies = tut.getPopularMovies();

		// then
		assertThat(popularMovies.getResults()).hasSizeGreaterThan(0);
	}

	@Test
	void should_get_movie_genres() {
		// when
		final MovieGenreResponse genres = tut.getGenres();

		// then
		assertThat(genres.getGenres()).hasSizeGreaterThan(0);
	}

	@Test
	void should_find_movies_by_genre_id() {
		// given
		final MovieGenreResponse genres = tut.getGenres();
		final MovieGenre genre = genres.getGenres().get(0);

		// when
		MovieInfoResponse moviesByGenre = tut.getMoviesByGenre(List.of(genre.getId()));

		// then
		assertThat(moviesByGenre.getResults()).isNotNull();
		assertThat(moviesByGenre.getResults()).hasSizeGreaterThan(0);
	}

}
package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest.FindMovieInfoByIdRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest.GetMovieInfoGenresRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest.GetMoviesByGenreIdRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external.rest.GetPopularMoviesInfoRestInvoker;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TMDBMovieInfoProviderIntTest {

	@Autowired
	private GetPopularMoviesInfoRestInvoker getPopularMoviesInfoRest;

	@Autowired
	private GetMovieInfoGenresRestInvoker getMovieInfoGenresRest;

	@Autowired
	private GetMoviesByGenreIdRestInvoker getMoviesByGenreIdRest;

	@Autowired
	private FindMovieInfoByIdRestInvoker findMovieInfoByIdRest;

	@Test
	void should_return_most_popular_movies_and_find_movie_by_id() {
		// when
		final ResponseEntity<List<MovieInfo>> getPopularMoviesResponse = getPopularMoviesInfoRest.getPopularMovies();

		// then
		assertThat(getPopularMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getPopularMoviesResponse.getBody()).hasSizeGreaterThan(0);

		// when
		final MovieInfo movieInfo = getPopularMoviesResponse.getBody().get(0);
		final ResponseEntity<MovieInfo> movieInfoByIdResponse = findMovieInfoByIdRest.getMovieInfoGenres(movieInfo.getId());

		// then
		assertThat(movieInfoByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(movieInfoByIdResponse.getBody()).isEqualTo(movieInfo);
	}

	@Test
	void should_find_movies_by_genre() {
		// when
		final ResponseEntity<List<Genre>> movieInfoGenresResponse = getMovieInfoGenresRest.getMovieInfoGenres();

		// then
		assertThat(movieInfoGenresResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(movieInfoGenresResponse.getBody()).hasSizeGreaterThan(0);

		// when
		final Long genreId = movieInfoGenresResponse.getBody().get(0).getId();
		final ResponseEntity<List<MovieInfo>> moviesByGenreIdResponse = getMoviesByGenreIdRest.getMoviesByGenreId(genreId);

		// then
		assertThat(moviesByGenreIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(moviesByGenreIdResponse.getBody()).hasSizeGreaterThan(0);

	}
}
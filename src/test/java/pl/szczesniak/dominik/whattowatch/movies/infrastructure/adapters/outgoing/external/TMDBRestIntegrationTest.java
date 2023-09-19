package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TMDBRestIntegrationTest {

	private final static String API_KEY = "?api_key=4663542b69490ce1434ece35ebad7665";

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void should_get_popular_movies() throws IOException {
		// given
		final HttpUriRequest getPopularMoviesRequest = new HttpGet("https://api.themoviedb.org/3/movie/popular" + API_KEY);

		// when
		final HttpResponse getPopularMoviesResponse = HttpClientBuilder.create().build().execute(getPopularMoviesRequest);
		final JsonNode responseBody = objectMapper.readTree(getPopularMoviesResponse.getEntity().getContent());

		// then
		assertThat(getPopularMoviesResponse.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseBody).isNotNull();
	}

	@Test
	void should_get_movie_genres() throws IOException {
		// given
		final HttpUriRequest getMovieGenresRequest = new HttpGet("https://api.themoviedb.org/3/genre/movie/list" + API_KEY);

		// when
		final HttpResponse getMovieGenresResponse = HttpClientBuilder.create().build().execute(getMovieGenresRequest);
		final JsonNode responseBody = objectMapper.readTree(getMovieGenresResponse.getEntity().getContent());

		// then
		assertThat(getMovieGenresResponse.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseBody).isNotNull();
	}

	@Test
	void should_find_movies_by_genre_id() throws IOException {
		// given
		final HttpUriRequest getMovieGenresRequest = new HttpGet("https://api.themoviedb.org/3/genre/movie/list" + API_KEY);
		final HttpResponse getMovieGenresResponse = HttpClientBuilder.create().build().execute(getMovieGenresRequest);
		final JsonNode responseBody = objectMapper.readTree(getMovieGenresResponse.getEntity().getContent());

		// when
		final String genreId = responseBody.get("genres").get(0).get("id").asText();
		final String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&api_key=4663542b69490ce1434ece35ebad7665&with_genres=";
		final String getMoviesByGenreIdUrl = url + genreId;
		final HttpUriRequest getMoviesByGenreIdRequest = new HttpGet(getMoviesByGenreIdUrl);
		final HttpResponse getMoviesByGenreIdResponse = HttpClientBuilder.create().build().execute(getMoviesByGenreIdRequest);
		final JsonNode moviesByIdResponse = objectMapper.readTree(getMoviesByGenreIdResponse.getEntity().getContent());

		// then
		assertThat(getMoviesByGenreIdResponse.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(moviesByIdResponse).isNotNull();
	}
}
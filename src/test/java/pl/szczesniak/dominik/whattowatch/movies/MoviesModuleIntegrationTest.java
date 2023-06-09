package pl.szczesniak.dominik.whattowatch.movies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szczesniak.dominik.whattowatch.movies.AddMovieRestInvoker.CreateMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.FindMoviesToWatchRestInvoker.MovieDto;
import pl.szczesniak.dominik.whattowatch.movies.FindWatchedMoviesRestInvoker.WatchedMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.UpdateMovieRestInvoker.UpdateMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.CreateUserRestInvoker;
import pl.szczesniak.dominik.whattowatch.users.CreateUserRestInvoker.CreateUserDto;
import pl.szczesniak.dominik.whattowatch.users.LoginUserRestInvoker;
import pl.szczesniak.dominik.whattowatch.users.LoginUserRestInvoker.LoginUserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MoviesModuleIntegrationTest {

	@Autowired
	private CreateUserRestInvoker createUserRest;

	@Autowired
	private LoginUserRestInvoker loginUserRest;

	@Autowired
	private FindMoviesToWatchRestInvoker findMoviesToWatchRest;

	@Autowired
	private RemoveMovieFromListRestInvoker removeMovieFromListRest;

	@Autowired
	private AddMovieRestInvoker addMovieRest;

	@Autowired
	private UpdateMovieRestInvoker updateMovieRest;

	@Autowired
	private FindWatchedMoviesRestInvoker findWatchedMoviesRest;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void should_add_movie_and_retrieve_list() {
		// given
		final ResponseEntity<Integer> loginUserResponse = getLoginUserResponse();

		final Integer userId = loginUserResponse.getBody();
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = getAddMovieResponse(userId, movieTitle);

		// when
		final HttpHeaders headers = getUserIdHeader(userId);

		final ResponseEntity<List<MovieDto>> findMoviesResponse = findMoviesToWatchRest.findMoviesToWatch(headers);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody()).satisfies(movie -> movie.forEach(movieDetails -> {
					assertThat(movieDetails.getTitle()).isEqualTo(movieTitle.getValue());
					assertThat(movieDetails.getUserId()).isEqualTo(userId);
					assertThat(movieDetails.getMovieId()).isEqualTo(addMovieResponse.getBody());
				}
		));
	}

	@Test
	void should_add_movie_and_delete_movie() {
		// given
		final ResponseEntity<Integer> loginUserResponse = getLoginUserResponse();

		final Integer userId = loginUserResponse.getBody();
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = getAddMovieResponse(userId, movieTitle);

		// when
		final HttpHeaders headers = getUserIdHeader(userId);

		final Integer movieId = addMovieResponse.getBody();

		final ResponseEntity<Void> deleteMovieResponse = removeMovieFromListRest.removeMovie(headers, void.class, movieId);

		// then
		assertThat(deleteMovieResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// when
		final HttpHeaders listHeaders = getUserIdHeader(userId);

		final ResponseEntity<List<MovieDto>> findMoviesResponse = findMoviesToWatchRest.findMoviesToWatch(listHeaders);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody()).isEmpty();

	}

	@Test
	void should_update_movie() {
		// given
		final ResponseEntity<Integer> loginUserResponse = getLoginUserResponse();

		final Integer userId = loginUserResponse.getBody();
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = getAddMovieResponse(userId, movieTitle);

		// when
		final HttpHeaders headers = getUserIdHeader(userId);
		final Integer movieId = addMovieResponse.getBody();
		final UpdateMovieDto updateMovieDto = UpdateMovieDto.builder().movieTitle(createAnyMovieTitle().getValue()).build();

		final ResponseEntity<Void> updateMovieResponse = updateMovieRest.updateMovie(headers, updateMovieDto, void.class, movieId);

		// then
		assertThat(updateMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// when
		final HttpHeaders listHeaders = getUserIdHeader(userId);

		final ResponseEntity<List<MovieDto>> findMoviesResponse = findMoviesToWatchRest.findMoviesToWatch(listHeaders);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody()).satisfies(movie -> movie.forEach(movieDetails -> {
					assertThat(movieDetails.getTitle()).isNotEqualTo(movieTitle.getValue());
					assertThat(movieDetails.getUserId()).isEqualTo(userId);
					assertThat(movieDetails.getMovieId()).isEqualTo(movieId);
				}
		));
	}

	@Test
	void should_move_movie_to_watched_list_and_retrieve_the_list() {
		// given
		final ResponseEntity<Integer> loginUserResponse = getLoginUserResponse();

		final Integer userId = loginUserResponse.getBody();
		final MovieTitle movieTitle = createAnyMovieTitle();
		final HttpHeaders userIdHeader = getUserIdHeader(userId);

		final ResponseEntity<Integer> addMovieResponse = getAddMovieResponse(userId, movieTitle);

		// when
		final ResponseEntity<Void> moveMovieToWatchedListResponse = restTemplate.exchange(
				"/api/movies/{movieId}/watched",
				HttpMethod.POST,
				new HttpEntity<>(userIdHeader),
				void.class,
				addMovieResponse.getBody()
		);

		// then
		assertThat(moveMovieToWatchedListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		final ResponseEntity<List<MovieDto>> findMoviesResponse = findMoviesToWatchRest.findMoviesToWatch(userIdHeader);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody()).isEmpty();

		// when
		final ResponseEntity<List<WatchedMovieDto>> getWatchedMoviesResponse = findWatchedMoviesRest.findWatchedMovies(userIdHeader);

		// then
		assertThat(getWatchedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getWatchedMoviesResponse.getBody()).satisfies(movie -> movie.forEach(movieDetails -> {
					assertThat(movieDetails.getTitle()).isEqualTo(movieTitle.getValue());
					assertThat(movieDetails.getUserId()).isEqualTo(userId);
					assertThat(movieDetails.getMovieId()).isEqualTo(addMovieResponse.getBody());
				}
		));

	}

	private ResponseEntity<Integer> getLoginUserResponse() {
		// when
		final CreateUserDto userToCreate = createAnyUser();
		final ResponseEntity<Integer> createUserResponse = createUserRest.createUser(userToCreate, Integer.class);


		final ResponseEntity<Integer> loginUserResponse = loginUserRest.loginUser(
				LoginUserDto.builder().username(userToCreate.getUsername()).password(userToCreate.getPassword()).build(),
				Integer.class
		);

		// then
		assertThat(createUserResponse.getBody()).isEqualTo(loginUserResponse.getBody());
		return loginUserResponse;
	}

	private ResponseEntity<Integer> getAddMovieResponse(final Integer userId, final MovieTitle movieTitle) {
		// when
		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				CreateMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertThat(addMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(addMovieResponse.getBody()).isNotNull();
		assertThat(addMovieResponse.getBody()).isGreaterThan(0);
		return addMovieResponse;
	}

	private static CreateUserDto createAnyUser() {
		return CreateUserDto.builder().username(createAnyUsername().getValue()).password(createAnyUserPassword().getValue()).build();
	}

	private static HttpHeaders getUserIdHeader(final Integer userId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("userId", String.valueOf(userId));
		return headers;
	}

}

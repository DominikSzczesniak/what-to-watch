package pl.szczesniak.dominik.whattowatch.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szczesniak.dominik.whattowatch.movies.AddCommentToMovieRestInvoker.AddCommentDto;
import pl.szczesniak.dominik.whattowatch.movies.AddMovieToWatchRestInvoker.AddMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.DeleteCommentFromMovieToWatchRestInvoker.DeleteCommentDto;
import pl.szczesniak.dominik.whattowatch.movies.FindAllMoviesToWatchRestInvoker.MovieDto;
import pl.szczesniak.dominik.whattowatch.movies.FindMovieToWatchRestInvoker.MovieDetailsDTO;
import pl.szczesniak.dominik.whattowatch.movies.FindWatchedMoviesRestInvoker.WatchedMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.UpdateMovieToWatchRestInvoker.UpdateMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.domain.UserProvider;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentSample.createAnyComment;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MoviesModuleIntegrationTest {

	@MockBean
	private UserProvider userProvider;

	@Autowired
	private FindAllMoviesToWatchRestInvoker findAllMoviesToWatchRest;

	@Autowired
	private FindMovieToWatchRestInvoker findMovieToWatchRest;

	@Autowired
	private RemoveMovieToWatchFromListRestInvoker removeMovieFromListRest;

	@Autowired
	private AddMovieToWatchRestInvoker addMovieRest;

	@Autowired
	private UpdateMovieToWatchRestInvoker updateMovieRest;

	@Autowired
	private FindWatchedMoviesRestInvoker findWatchedMoviesRest;

	@Autowired
	private MoveMovieToWatchToWatchedListInvoker moveMovieToWatchToWatchedListRest;

	@Autowired
	private AddCommentToMovieRestInvoker addCommentToMovieRest;

	@Autowired
	private DeleteCommentFromMovieToWatchRestInvoker deleteCommentFromMovieToWatchRest;

	private Integer userId;

	@BeforeEach
	void setUp() {
		given(userProvider.exists(any())).willReturn(true);
		userId = createAnyUserId().getValue();
	}

	@Test
	void should_add_movie_and_retrieve_list() {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertThat(addMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(addMovieResponse.getBody()).isNotNull();
		assertThat(addMovieResponse.getBody()).isGreaterThan(0);

		// when
		final ResponseEntity<List<MovieDto>> findMoviesResponse = findAllMoviesToWatchRest.findAllMoviesToWatch(userId);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody())
				.extracting(MovieDto::getTitle, MovieDto::getUserId, MovieDto::getMovieId)
				.containsExactly(tuple(movieTitle.getValue(), userId, addMovieResponse.getBody()));
	}

	@Test
	void should_add_movie_and_delete_movie() {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final Integer movieId = addMovieResponse.getBody();

		final ResponseEntity<Void> deleteMovieResponse = removeMovieFromListRest.removeMovie(userId, movieId);

		// then
		assertThat(deleteMovieResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// when
		final ResponseEntity<List<MovieDto>> findMoviesResponse = findAllMoviesToWatchRest.findAllMoviesToWatch(userId);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody()).isEmpty();
	}

	@Test
	void should_update_movie() {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final Integer movieId = addMovieResponse.getBody();
		final MovieTitle changedTitle = createAnyMovieTitle();
		final UpdateMovieDto updateMovieDto = UpdateMovieDto.builder().movieTitle(changedTitle.getValue()).build();

		final ResponseEntity<Void> updateMovieResponse = updateMovieRest.updateMovie(updateMovieDto, userId, movieId);

		// then
		assertThat(updateMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// when
		final ResponseEntity<List<MovieDto>> findMoviesResponse = findAllMoviesToWatchRest.findAllMoviesToWatch(userId);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody())
				.extracting(MovieDto::getTitle, MovieDto::getUserId, MovieDto::getMovieId)
				.containsExactly(tuple(changedTitle.getValue(), userId, movieId));
	}

	@Test
	void should_move_movie_to_watched_list_and_retrieve_the_list() {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final ResponseEntity<Void> moveMovieToWatchedListResponse = moveMovieToWatchToWatchedListRest.findMoviesToWatch(
				userId,
				addMovieResponse.getBody()
		);

		// then
		assertThat(moveMovieToWatchedListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		final ResponseEntity<List<MovieDto>> findMoviesResponse = findAllMoviesToWatchRest.findAllMoviesToWatch(userId);

		// then
		assertThat(findMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMoviesResponse.getBody()).isEmpty();

		// when
		final ResponseEntity<List<WatchedMovieDto>> getWatchedMoviesResponse = findWatchedMoviesRest.findWatchedMovies(userId);

		// then
		assertThat(getWatchedMoviesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getWatchedMoviesResponse.getBody())
				.extracting(WatchedMovieDto::getTitle, WatchedMovieDto::getUserId, WatchedMovieDto::getMovieId)
				.containsExactly(tuple(movieTitle.getValue(), userId, addMovieResponse.getBody()));
	}

	@Test
	void should_add_comment_to_movie_and_delete_the_comment() {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final String comment = createAnyComment();
		final ResponseEntity<String> addCommentToMovieResponse = addCommentToMovieRest.addCommentToMovie(
				userId,
				addMovieResponse.getBody(),
				new AddCommentDto(comment)
		);

		// then
		assertThat(addCommentToMovieResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(addCommentToMovieResponse.getBody()).isNotNull();

		// when
		final ResponseEntity<MovieDetailsDTO> findMovieToWatchResponse = findMovieToWatchRest.findMovieToWatch(userId, addMovieResponse.getBody());

		// then
		assertThat(findMovieToWatchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMovieToWatchResponse.getBody().getComments()).hasSize(1);
		assertThat(findMovieToWatchResponse.getBody().getComments().get(addCommentToMovieResponse.getBody())).contains(comment);
		assertThat(findMovieToWatchResponse.getBody().getMovieId()).isEqualTo(addMovieResponse.getBody());

		// when
		final ResponseEntity<Void> deleteCommentFromMovieToWatchResponse = deleteCommentFromMovieToWatchRest.deleteCommentFromMovieToWatch(
				userId,
				addMovieResponse.getBody(),
				new DeleteCommentDto(addCommentToMovieResponse.getBody())
		);

		// then
		assertThat(deleteCommentFromMovieToWatchResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// when
		final ResponseEntity<MovieDetailsDTO> findMovieToWatchResponseAfterDeletingComment = findMovieToWatchRest.findMovieToWatch(
				userId,
				addMovieResponse.getBody()
		);

		// then
		assertThat(findMovieToWatchResponseAfterDeletingComment.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMovieToWatchResponseAfterDeletingComment.getBody().getComments()).hasSize(0);
	}

	private static void assertMovieWasAdded(final ResponseEntity<Integer> addMovieResponse) {
		assertThat(addMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(addMovieResponse.getBody()).isNotNull();
		assertThat(addMovieResponse.getBody()).isGreaterThan(0);
	}

}

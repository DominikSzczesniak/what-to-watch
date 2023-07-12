package pl.szczesniak.dominik.whattowatch.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.szczesniak.dominik.whattowatch.movies.domain.UserProvider;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddCommentToMovieRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddCommentToMovieRestInvoker.CommentDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieToWatchRestInvoker.AddMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteCommentFromMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteCommentFromMovieToWatchRestInvoker.DeleteCommentDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteMovieToWatchCoverRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMoviesToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMoviesToWatchRestInvoker.MovieDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindMovieToWatchRestInvoker.MovieCommentDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindMovieToWatchRestInvoker.MovieDetailsDTO;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.GetMovieToWatchCoverRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.RemoveMovieToWatchFromListRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.SetMovieToWatchCoverRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.UpdateMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.UpdateMovieToWatchRestInvoker.UpdateMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies.FindWatchedMoviesRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies.FindWatchedMoviesRestInvoker.WatchedMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.watchedmovies.MoveMovieToWatchToWatchedListInvoker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
	private SetMovieToWatchCoverRestInvoker setMovieToWatchCoverRest;

	@Autowired
	private GetMovieToWatchCoverRestInvoker getMovieToWatchCoverRest;

	@Autowired
	private DeleteMovieToWatchCoverRestInvoker deleteMovieToWatchCoverRest;

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
				CommentDto.builder().comment(comment).build()
		);

		// then
		assertThat(addCommentToMovieResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(addCommentToMovieResponse.getBody()).isNotNull();

		// when
		final ResponseEntity<MovieDetailsDTO> findMovieToWatchResponse = findMovieToWatchRest.findMovieToWatch(userId, addMovieResponse.getBody());

		// then
		final MovieDetailsDTO movieDetails = findMovieToWatchResponse.getBody();
		assertThat(findMovieToWatchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(movieDetails.getComments()).hasSize(1);
		assertThat(movieDetails.getComments().get(0)).extracting(MovieCommentDto::getValue).isEqualTo(comment);
		assertThat(movieDetails.getMovieId()).isEqualTo(addMovieResponse.getBody());

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

	@Test
	void should_save_cover_and_delete_it() throws IOException {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final File coverFile = new File("src/test/resources/imagesTest/2x.png");
		final String filename = coverFile.getName();
		final String originalFilename = coverFile.getName();
		final String contentType = "image/png";
		final byte[] content = Files.readAllBytes(coverFile.toPath());

		final MultipartFile multipartFile = new MockMultipartFile(filename, originalFilename, contentType, content);
		final ResponseEntity<?> setMovieToWatchResponse = setMovieToWatchCoverRest.setCover(userId, addMovieResponse.getBody(), multipartFile);

		// then
		assertThat(setMovieToWatchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// when
		final ResponseEntity<byte[]> getMovietoWatchCoverResponse = getMovieToWatchCoverRest.getMovieToWatchCover(userId, addMovieResponse.getBody());

		// then
		final HttpHeaders headers = getMovietoWatchCoverResponse.getHeaders();
		final String contentTypeHeader = headers.getFirst(HttpHeaders.CONTENT_TYPE);
		final String contentDispositionHeader = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);

		assertThat(getMovietoWatchCoverResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getMovietoWatchCoverResponse.getBody()).containsExactly(multipartFile.getInputStream().readAllBytes());
		assertThat(contentTypeHeader).isEqualTo(contentType);
		assertThat(contentDispositionHeader).isEqualTo("attachment; filename=\"" + filename + "\"");

		// when
		final ResponseEntity<Void> deleteMovieToWatchCoverResponse = deleteMovieToWatchCoverRest.deleteMovieToWatchCover(userId, addMovieResponse.getBody());

		// then
		assertThat(deleteMovieToWatchCoverResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// when
		final ResponseEntity<byte[]> getMovietoWatchCoverResponseAfterDeleting = getMovieToWatchCoverRest
				.getMovieToWatchCover(userId, addMovieResponse.getBody());

		// then
		assertThat(getMovietoWatchCoverResponseAfterDeleting.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private static void assertMovieWasAdded(final ResponseEntity<Integer> addMovieResponse) {
		assertThat(addMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(addMovieResponse.getBody()).isNotNull();
		assertThat(addMovieResponse.getBody()).isGreaterThan(0);
	}

}

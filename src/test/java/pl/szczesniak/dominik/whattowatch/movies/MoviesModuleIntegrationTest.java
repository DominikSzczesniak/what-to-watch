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
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddCommentToMovieRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddCommentToMovieRestInvoker.CommentDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieTagToMovieRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieTagToMovieRestInvoker.MovieTagDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.AddMovieToWatchRestInvoker.AddMovieDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteCommentFromMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteCommentFromMovieToWatchRestInvoker.DeleteCommentDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteMovieTagFromMovieRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.DeleteMovieToWatchCoverRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMovieIdsByMovieTagIdRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMovieTagsRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMovieTagsRestInvoker.FoundMovieTagDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMoviesToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindAllMoviesToWatchRestInvoker.MovieDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindMovieToWatchRestInvoker;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindMovieToWatchRestInvoker.MovieCommentDto;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies.FindMovieToWatchRestInvoker.MovieDetailsDto;
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
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabelSample.createAnyTagLabel;
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

	@Autowired
	private AddMovieTagToMovieRestInvoker addMovieTagToMovieRest;

	@Autowired
	private DeleteMovieTagFromMovieRestInvoker deleteMovieTagFromMovieRest;

	@Autowired
	private FindAllMovieTagsRestInvoker findMovieTagsRest;

	@Autowired
	private FindAllMovieIdsByMovieTagIdRestInvoker findAllMovieIdsByMovieTagIdRest;

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
		final ResponseEntity<MovieDetailsDto> findMovieToWatchResponse = findMovieToWatchRest.findMovieToWatch(userId, addMovieResponse.getBody());

		// then
		final MovieDetailsDto movieDetails = findMovieToWatchResponse.getBody();
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
		final ResponseEntity<MovieDetailsDto> findMovieToWatchResponseAfterDeletingComment = findMovieToWatchRest.findMovieToWatch(
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
		final ResponseEntity<byte[]> getMovieToWatchCoverResponse = getMovieToWatchCoverRest.getMovieToWatchCover(userId, addMovieResponse.getBody());

		// then
		final HttpHeaders headers = getMovieToWatchCoverResponse.getHeaders();
		final String contentTypeHeader = headers.getFirst(HttpHeaders.CONTENT_TYPE);
		final String contentDispositionHeader = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);

		assertThat(getMovieToWatchCoverResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getMovieToWatchCoverResponse.getBody()).containsExactly(multipartFile.getInputStream().readAllBytes());
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


	@Test
	void should_add_tag_to_a_movie_then_find_tags_and_delete_tag_from_movie() {
		// given
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final String tagLabel = createAnyTagLabel().getValue();
		final ResponseEntity<String> addMovieTagToMovieResponse = addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagLabel(tagLabel).build(), userId, addMovieResponse.getBody());

		// then
		assertThat(addMovieTagToMovieResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// when
		final ResponseEntity<MovieDetailsDto> findMovieToWatchResponse = findMovieToWatchRest.findMovieToWatch(userId, addMovieResponse.getBody());

		// then
		final MovieDetailsDto movieDetails = findMovieToWatchResponse.getBody();
		assertThat(findMovieToWatchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(movieDetails.getTags()).hasSize(1);
		assertThat(movieDetails.getTags().get(0)).extracting(FindMovieToWatchRestInvoker.MovieTagDto::getTagLabel).isEqualTo(tagLabel);
		assertThat(movieDetails.getMovieId()).isEqualTo(addMovieResponse.getBody());

		// when
		final ResponseEntity<List<FoundMovieTagDto>> findMovieTagsResponse = findMovieTagsRest.getMovieTagsByUserId(userId);

		// then
		assertThat(findMovieTagsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findMovieTagsResponse.getBody()).hasSize(1);

		// when
		final ResponseEntity<Void> deleteMovieTagFromMovieResponse = deleteMovieTagFromMovieRest.deleteTagFromMovie(
				addMovieTagToMovieResponse.getBody(),
				userId,
				addMovieResponse.getBody()
		);

		// then
		assertThat(deleteMovieTagFromMovieResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// when
		final ResponseEntity<MovieDetailsDto> findMovieToWatchResponseAfterDeletingTag =
				findMovieToWatchRest.findMovieToWatch(userId, addMovieResponse.getBody());

		// then
		final MovieDetailsDto movieDetailsAfterDeletingTag = findMovieToWatchResponseAfterDeletingTag.getBody();
		assertThat(findMovieToWatchResponseAfterDeletingTag.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(movieDetailsAfterDeletingTag.getTags()).hasSize(0);
		assertThat(movieDetailsAfterDeletingTag.getMovieId()).isEqualTo(addMovieResponse.getBody());
	}

	@Test
	void user_should_not_be_able_to_add_not_his_movie_tag_to_his_movie() {
		// given
		final Integer differentUser = createAnyUserId().getValue();
		final MovieTitle movieTitle = createAnyMovieTitle();

		final ResponseEntity<Integer> addMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().title(movieTitle.getValue()).userId(userId).build(),
				Integer.class
		);

		final ResponseEntity<Integer> differentMovieAddMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().userId(differentUser).build(),
				Integer.class
		);

		// then
		assertMovieWasAdded(addMovieResponse);

		// when
		final String tagLabel = createAnyTagLabel().getValue();
		final ResponseEntity<String> addMovieTagToMovieResponse = addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagLabel(tagLabel).build(), userId, addMovieResponse.getBody());


		final ResponseEntity<String> addingSameTagLabelToMovieByDifferentUserResponse = addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagLabel(tagLabel).build(), differentUser, differentMovieAddMovieResponse.getBody()
		);

		// then
		assertThat(addMovieTagToMovieResponse.getBody()).isNotEqualTo(addingSameTagLabelToMovieByDifferentUserResponse.getBody());

		// when
		final ResponseEntity<String> addingUnauthorizedUserMovieTagToMovie = addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagId(addMovieTagToMovieResponse.getBody()).build(), differentUser, differentMovieAddMovieResponse.getBody()
		);

		// then
		assertThat(addingUnauthorizedUserMovieTagToMovie.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		// when
		ResponseEntity<MovieDetailsDto> secondUsersMovieToWatchResponse = findMovieToWatchRest.findMovieToWatch(differentUser, differentMovieAddMovieResponse.getBody());

		// then
		assertThat(secondUsersMovieToWatchResponse.getBody().getTags()).hasSize(1);
	}

	@Test
	void should_find_all_user_movies_with_tag_id() {
		// given
		final Integer differentUser = createAnyUserId().getValue();

		final ResponseEntity<Integer> addMovieResponse1 = addMovieRest.addMovie(
				AddMovieDto.builder().userId(userId).build(),
				Integer.class
		);

		final ResponseEntity<Integer> addMovieResponse2 = addMovieRest.addMovie(
				AddMovieDto.builder().userId(userId).build(),
				Integer.class
		);

		final ResponseEntity<Integer> differentUsersMovieResponse = addMovieRest.addMovie(
				AddMovieDto.builder().userId(differentUser).build(),
				Integer.class
		);

		final ResponseEntity<String> addTagToMovieOneResponse = addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagLabel(createAnyTagLabel().getValue()).build(), userId, addMovieResponse1.getBody());

		addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagId(addTagToMovieOneResponse.getBody()).build(), userId, addMovieResponse2.getBody());

		addMovieTagToMovieRest.addTagToMovie(
				MovieTagDto.builder().tagLabel(createAnyTagLabel().getValue()).build(), differentUser, differentUsersMovieResponse.getBody());

		// when
		final ResponseEntity<List<MovieId>> findAllMoviesByTagIdResponse = // TODO: fix
				findAllMovieIdsByMovieTagIdRest.getMoviesByTagId(userId, addTagToMovieOneResponse.getBody());

		// then
		assertThat(findAllMoviesByTagIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findAllMoviesByTagIdResponse.getBody()).hasSize(2);
	}

	private static void assertMovieWasAdded(final ResponseEntity<Integer> addMovieResponse) {
		assertThat(addMovieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(addMovieResponse.getBody()).isNotNull();
		assertThat(addMovieResponse.getBody()).isGreaterThan(0);
	}

}


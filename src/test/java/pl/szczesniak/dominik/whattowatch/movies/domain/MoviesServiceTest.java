package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieComment;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCoverSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovieSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesToWatchServiceConfiguration.moviesToWatchService;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentSample.createAnyComment;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverContentSample.createAnyCoverContent;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverContentTypeSample.createAnyContentType;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverFilenameSample.createAnyCoverFilename;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabelSample.createAnyTagLabel;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

class MoviesServiceTest {

	private InMemoryUserProvider userProvider;

	private MoviesService tut;

	@BeforeEach
	void setUp() {
		userProvider = new InMemoryUserProvider();
		tut = moviesToWatchService(userProvider);
	}

	@Test
	void user_should_add_movie_to_his_list() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		assertThat(tut.getMoviesToWatch(userId)).hasSize(0);

		// when
		tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());

		// then
		assertThat(tut.getMoviesToWatch(userId)).hasSize(1);
	}

	@Test
	void should_not_add_movie_when_user_doesnt_exist() {
		// given
		final UserId userId = createAnyUserId();

		// when
		final Throwable thrown = catchThrowable(() -> tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void user_should_delete_movie_from_his_list() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final MovieId addedMovieId = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());
		final MovieId movieToRemove = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());

		// when
		tut.removeMovieFromList(movieToRemove, userId);

		// then
		assertThat(tut.getMoviesToWatch(userId)).hasSize(1)
				.extracting(Movie::getMovieId)
				.containsExactly(addedMovieId);
	}

	@Test
	void should_not_be_able_to_get_movies_to_watch_list_when_user_does_not_exist() {
		// when
		final Throwable thrown = catchThrowable(() -> tut.getMoviesToWatch(createAnyUserId()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void user_should_be_able_to_add_movies_with_same_title() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final AddMovieToList createdMovie = AddMovieToListSample.builder().userId(userId).build();
		final AddMovieToList movieWithSameTitle = AddMovieToListSample.builder().movieTitle(createdMovie.getMovieTitle()).userId(userId).build();

		// when
		tut.addMovieToList(createdMovie);
		tut.addMovieToList(movieWithSameTitle);

		// then
		assertThat(tut.getMoviesToWatch(userId)).hasSize(2)
				.extracting(Movie::getTitle)
				.containsExactlyInAnyOrder(createdMovie.getMovieTitle(), movieWithSameTitle.getMovieTitle());
	}

	@Test
	void only_one_movie_with_duplicated_titles_should_be_deleted() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final AddMovieToList movie = AddMovieToListSample.builder().userId(userId).build();

		final MovieId movieId = tut.addMovieToList(movie);
		final MovieId duplicatedTitleMovieId = tut.addMovieToList(AddMovieToListSample.builder().movieTitle(movie.getMovieTitle()).userId(userId).build());

		// when
		tut.removeMovieFromList(duplicatedTitleMovieId, userId);

		// then
		assertThat(tut.getMoviesToWatch(userId)).hasSize(1)
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(movieId);
	}

	@Test
	void two_different_users_should_have_different_lists() {
		// given
		final UserId userIdOne = userProvider.addUser(createAnyUserId());
		final UserId userIdTwo = userProvider.addUser(createAnyUserId());

		// when
		final MovieId firstUserMovieOne = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdOne).build());
		final MovieId secondUserMovie = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdTwo).build());
		final MovieId firstUserMovieTwo = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdOne).build());

		// then
		assertThat(tut.getMoviesToWatch(userIdOne))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(firstUserMovieOne, firstUserMovieTwo);

		assertThat(tut.getMoviesToWatch(userIdTwo))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(secondUserMovie);
	}

	@Test
	void should_not_delete_movie_if_not_users_movie() {
		// given
		final UserId userIdOne = userProvider.addUser(createAnyUserId());
		final UserId userIdTwo = userProvider.addUser(createAnyUserId());

		tut.addMovieToList(AddMovieToListSample.builder().userId(userIdOne).build());
		tut.addMovieToList(AddMovieToListSample.builder().userId(userIdOne).build());
		final MovieId movieToDelete = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdTwo).build());

		// when
		tut.removeMovieFromList(movieToDelete, userIdOne);

		// then
		assertThat(tut.getMoviesToWatch(userIdOne)).hasSize(2);
		assertThat(tut.getMoviesToWatch(userIdTwo)).hasSize(1);
	}

	@Test
	void should_delete_movie_only_from_this_user_list_and_not_all_of_them() {
		// given
		final UserId userIdOne = userProvider.addUser(createAnyUserId());
		final UserId userIdTwo = userProvider.addUser(createAnyUserId());

		final MovieId movieToDelete = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdOne).build());
		final MovieId secondUserMovie = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdTwo).build());
		final MovieId firstUserMovie = tut.addMovieToList(AddMovieToListSample.builder().userId(userIdOne).build());

		// when
		tut.removeMovieFromList(movieToDelete, userIdOne);

		// then
		assertThat(tut.getMoviesToWatch(userIdOne))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(firstUserMovie);

		assertThat(tut.getMoviesToWatch(userIdTwo))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(secondUserMovie);
	}

	@Test
	void list_should_be_empty_when_no_movie_added() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		// when
		final List<Movie> movies = tut.getMoviesToWatch(userId);

		// then
		assertThat(movies.isEmpty()).isTrue();
	}

	@Test
	void should_add_movie_to_watched_list_and_remove_from_movies_to_watch_list() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		final AddMovieToList movieOne = AddMovieToListSample.builder().userId(userId).build();
		final MovieId movieOneId = tut.addMovieToList(movieOne);

		final AddMovieToList movieTwo = AddMovieToListSample.builder().userId(userId).build();
		final MovieId movieTwoId = tut.addMovieToList(movieTwo);

		final AddMovieToList movieThree = AddMovieToListSample.builder().userId(userId).build();
		final MovieId movieThreeId = tut.addMovieToList(movieThree);

		// when
		tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().movieId(movieTwoId).userId(userId).build());

		// then
		assertThat(tut.getMoviesToWatch(userId))
				.extracting(Movie::getMovieId, Movie::getTitle)
				.containsExactlyInAnyOrder(
						tuple(movieOneId, movieOne.getMovieTitle()),
						tuple(movieThreeId, movieThree.getMovieTitle())
				);

		assertThat(tut.getWatchedMovies(userId))
				.extracting(WatchedMovie::getMovieId, WatchedMovie::getTitle)
				.containsExactlyInAnyOrder(tuple(movieTwoId, movieTwo.getMovieTitle()));
	}

	@Test
	void should_throw_exception_when_trying_to_move_the_movie_when_user_does_not_exist() {
		// when
		final Throwable thrown = catchThrowable(() -> tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_trying_to_move_the_movie_to_watched_list_when_movie_does_not_belong_to_user() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final UserId notQualifiedUser = userProvider.addUser(createAnyUserId());

		final MovieId movieToMove = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder()
				.movieId(movieToMove).userId(notQualifiedUser).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_trying_to_move_the_movie_when_movie_id_doesnt_exist() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		// when
		final Throwable thrown = catchThrowable(() -> tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_not_be_able_to_add_previously_removed_movie_to_watched_movies_list() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		final MovieId starWars = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());
		tut.removeMovieFromList(starWars, userId);

		// when
		final Throwable thrown = catchThrowable(() -> tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().movieId(starWars).userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_update_movie_title() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final MovieTitle title = createAnyMovieTitle();
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().movieTitle(title).userId(userId).build());

		final MovieTitle changedTitle = new MovieTitle("Star Wars");

		// when
		tut.updateMovie(UpdateMovieSample.builder().movieId(movieId).userId(userId).movieTitle(changedTitle).build());

		// then
		assertThat(tut.getMoviesToWatch(userId))
				.extracting(Movie::getTitle)
				.containsExactly(changedTitle);
	}

	@Test
	void should_throw_exception_when_trying_to_update_to_illegal_title() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final MovieTitle title = createAnyMovieTitle();
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().movieTitle(title).userId(userId).build());

		// when
		final Throwable thrownOne = catchThrowable(() -> tut.updateMovie(UpdateMovieSample.builder()
				.movieId(movieId)
				.userId(userId)
				.movieTitle(new MovieTitle(" "))
				.build()));
		final Throwable thrownTwo = catchThrowable(() -> tut.updateMovie(UpdateMovieSample.builder()
				.movieId(movieId)
				.userId(userId)
				.movieTitle(new MovieTitle(";"))
				.build()));

		// then
		assertThat(thrownOne).isInstanceOf(IllegalArgumentException.class);
		assertThat(thrownTwo).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_throw_exception_when_trying_to_change_title_of_nonexistent_movie() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		// when
		final Throwable thrown = catchThrowable(() -> tut.updateMovie(UpdateMovieSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_not_be_able_to_change_title_when_not_users_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());

		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.updateMovie(UpdateMovieSample.builder().movieId(movieId).userId(differentUser).build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_add_cover_to_movie() throws IOException {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		byte[] coverContent = createAnyCoverContent();

		// when
		final SetMovieCover coverCommand = SetMovieCoverSample.builder()
				.movieId(movieId)
				.coverContent(new ByteArrayInputStream(coverContent))
				.userId(user)
				.build();
		tut.setMovieCover(coverCommand);

		// then
		final MovieCoverDTO cover = tut.getCoverForMovie(movieId, user);
		assertThat(cover.getCoverContentType()).isEqualTo(coverCommand.getCoverContentType());
		assertThat(cover.getFilename()).isEqualTo(coverCommand.getCoverFilename());
		assertThat(cover.getCoverContent().readAllBytes()).containsExactly(coverContent);
	}

	@Test
	void should_change_movie_cover() throws IOException {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		tut.setMovieCover(SetMovieCoverSample.builder().movieId(movieId).userId(user).build());

		final String changedCoverFilename = createAnyCoverFilename();
		final String changedCoverContentType = createAnyContentType();
		byte[] changedCoverContent = createAnyCoverContent();

		// when
		tut.setMovieCover(SetMovieCoverSample.builder()
				.movieId(movieId)
				.userId(user)
				.coverFilename(changedCoverFilename)
				.coverContentType(changedCoverContentType)
				.coverContent(new ByteArrayInputStream(changedCoverContent))
				.build());

		// then
		final MovieCoverDTO coverAfterChange = tut.getCoverForMovie(movieId, user);
		assertThat(coverAfterChange.getCoverContentType()).isEqualTo(changedCoverContentType);
		assertThat(coverAfterChange.getFilename()).isEqualTo(changedCoverFilename);
		assertThat(coverAfterChange.getCoverContent().readAllBytes()).containsExactly(changedCoverContent);
	}

	@Test
	void should_delete_movie_cover() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		tut.setMovieCover(SetMovieCoverSample.builder().movieId(movieId).userId(user).build());
		assertThat(tut.getCoverForMovie(movieId, user)).isNotNull();

		// when
		tut.deleteCover(movieId, user);

		// then
		final Throwable thrown = catchThrowable(() -> tut.getCoverForMovie(movieId, user));
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_be_able_to_get_stored_file_content_multiple_times() throws IOException {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		tut.setMovieCover(SetMovieCoverSample.builder().movieId(movieId).userId(user).build());

		// when
		final MovieCoverDTO firstCover = tut.getCoverForMovie(movieId, user);
		final MovieCoverDTO secondCover = tut.getCoverForMovie(movieId, user);

		// then
		final byte[] expected = firstCover.getCoverContent().readAllBytes();
		final byte[] actual = secondCover.getCoverContent().readAllBytes();

		assertThat(actual).containsExactly(expected);
	}

	@Test
	void should_add_comment_to_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		// when
		final String comment = createAnyComment();
		tut.addCommentToMovie(AddCommentToMovieSample.builder().userId(user).movieId(movieId).comment(comment).build());

		// then
		final Set<MovieComment> comments = tut.getMovie(movieId, user).getComments();
		assertThat(comments).extracting(MovieComment::getText).containsExactly(comment);
	}

	@Test
	void should_not_add_comment_to_not_users_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		// when
		final String anyComment = createAnyComment();
		final Throwable thrown = catchThrowable(() -> tut.addCommentToMovie(AddCommentToMovieSample.builder()
				.userId(differentUser).movieId(movieId).comment(anyComment)
				.build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_delete_comment() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());

		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final UUID commentId = tut.addCommentToMovie(AddCommentToMovieSample.builder()
				.userId(user).movieId(movieId)
				.build());
		tut.addCommentToMovie(AddCommentToMovieSample.builder().userId(user).movieId(movieId).build());

		// when
		tut.deleteCommentFromMovie(DeleteCommentFromMovieSample.builder()
				.userId(user).movieId(movieId).commentId(new CommentId(commentId))
				.build());

		// then
		final Set<MovieComment> comments = tut.getMovie(movieId, user).getComments();
		assertThat(comments.size()).isEqualTo(1);
	}

	@Test
	void should_not_delete_comment_from_not_users_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final UUID commentId = tut.addCommentToMovie(AddCommentToMovieSample.builder()
				.userId(user).movieId(movieId)
				.build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.deleteCommentFromMovie(DeleteCommentFromMovieSample.builder()
				.userId(differentUser).movieId(movieId).commentId(new CommentId(commentId))
				.build()));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_add_tag_to_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		// when
		final TagLabel tagLabel = createAnyTagLabel();
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movieId).userId(user).build());

		// then
		final Set<MovieTag> tags = tut.getMovie(movieId, user).getTags();
		assertThat(tags).extracting(MovieTag::getLabel).containsExactly(tagLabel);
	}

	@Test
	void should_find_all_tags() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final TagLabel firstTagLabel = createAnyTagLabel();
		final TagLabel secondTagLabel = createAnyTagLabel();
		final TagLabel thirdTagLabel = createAnyTagLabel();
		final TagLabel fourthTagLabel = createAnyTagLabel();

		// when
		final TagId firstTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(firstTagLabel).movieId(movieId).userId(user).build());
		final TagId secondTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(secondTagLabel).movieId(movieId).userId(user).build());
		final TagId thirdTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(thirdTagLabel).movieId(movieId).userId(user).build());
		final TagId fourthTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(fourthTagLabel).movieId(movieId).userId(user).build());

		final List<MovieTag> tags = tut.getMovieTagsByUserId(user.getValue());

		// then
		assertThat(tags.size()).isEqualTo(4);
		assertThat(tags).extracting(MovieTag::getTagId).containsExactlyInAnyOrder(firstTagId, secondTagId, thirdTagId, fourthTagId);
		assertThat(tags).extracting(MovieTag::getLabel).containsExactlyInAnyOrder(firstTagLabel, secondTagLabel, thirdTagLabel, fourthTagLabel);
	}

	@Test
	void should_delete_tag_from_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		final TagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().movieId(movieId).userId(user).build());
		assertThat(tut.getMovie(movieId, user).getTags()).hasSize(1);

		// when
		tut.deleteTagFromMovie(DeleteTagFromMovieSample.builder().tagId(tagId).movieId(movieId).userId(user).build());

		// then
		assertThat(tut.getMovie(movieId, user).getTags()).hasSize(0);
	}

	@Test
	void should_not_delete_tag_from_movie_if_not_users_tag() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		final TagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().movieId(movieId).userId(user).build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.deleteTagFromMovie(new DeleteTagFromMovie(differentUser, movieId, tagId)));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_not_create_additional_movie_tag_when_same_tag_label() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final TagLabel tagLabel = createAnyTagLabel();

		// when
		final TagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movieId).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).tagId(tagId).movieId(movieId).userId(user).build());
		final Movie movie = tut.getMovie(movieId, user);

		// then
		assertThat(movie.getTags()).hasSize(1);
	}

	@Test
	void should_find_movies_by_tag() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movie1 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie2 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie3 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie4 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movieWithDifferentTag = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final TagLabel tagLabel = createAnyTagLabel();

		// when
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movie1).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movie2).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movie3).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movie4).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().movieId(movieWithDifferentTag).userId(user).build());

		// then
		assertThat(tut.getMoviesToWatch(user)).hasSize(5);
		assertThat(tut.getMoviesByTagLabel(tagLabel, user)).hasSize(4);
	}

	@Test
	void should_return_empty_list_when_no_movies_with_tag() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final TagLabel tagLabel = createAnyTagLabel();

		// when
		final List<Movie> moviesByTagLabel = tut.getMoviesByTagLabel(tagLabel, user);

		// then
		assertThat(moviesByTagLabel).hasSize(0);
	}

	@Test
	void user_should_not_be_able_to_add_not_his_movie_tag_to_his_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId differentUsersMovie = tut.addMovieToList(AddMovieToListSample.builder().userId(differentUser).build());

		// when
		final TagLabel tagLabel = createAnyTagLabel();
		final TagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movieId).userId(user).build());

		final Throwable thrown = catchThrowable(() -> tut.addTagToMovie
				(AddTagToMovieSample.builder().tagId(tagId).movieId(differentUsersMovie).tagLabel(tagLabel).userId(differentUser).build())
		);

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}
}
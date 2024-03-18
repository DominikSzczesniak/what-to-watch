package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieCommentQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesFacadeConfiguration.moviesFacade;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentSample.createAnyComment;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class MoviesFacadeCommentsTest {

	private InMemoryUserProvider userProvider;
	private MoviesFacade tut;

	@BeforeEach
	void setUp() {
		userProvider = new InMemoryUserProvider();
		tut = moviesFacade(userProvider);
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
		final Set<MovieCommentQueryResult> comments = tut.getMovie(movieId, user).getComments();
		assertThat(comments).extracting(MovieCommentQueryResult::getText).containsExactly(comment);
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
		final Set<MovieCommentQueryResult> comments = tut.getMovie(movieId, user).getComments();
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

}

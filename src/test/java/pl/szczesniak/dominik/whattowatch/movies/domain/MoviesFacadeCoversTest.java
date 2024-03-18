package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCover;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.SetMovieCoverSample;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieCoverDTO;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesFacadeConfiguration.moviesFacade;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverContentSample.createAnyCoverContent;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverContentTypeSample.createAnyContentType;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverFilenameSample.createAnyCoverFilename;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class MoviesFacadeCoversTest {

	private InMemoryUserProvider userProvider;
	private MoviesFacade tut;

	@BeforeEach
	void setUp() {
		userProvider = new InMemoryUserProvider();
		tut = moviesFacade(userProvider);
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
	void should_delete_all_users_movie_covers() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId_1 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movieId_2 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		tut.setMovieCover(SetMovieCoverSample.builder().movieId(movieId_1).userId(user).build());
		tut.setMovieCover(SetMovieCoverSample.builder().movieId(movieId_2).userId(user).build());

		// when
		tut.handleUserDeleted(user);

		// then
		final Throwable thrown_1 = catchThrowable(() -> tut.getCoverForMovie(movieId_1, user));
		final Throwable thrown_2 = catchThrowable(() -> tut.getCoverForMovie(movieId_2, user));
		assertThat(thrown_1).isInstanceOf(ObjectDoesNotExistException.class);
		assertThat(thrown_2).isInstanceOf(ObjectDoesNotExistException.class);
	}

}

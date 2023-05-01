package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.AddMovieToListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MoveMovieToWatchListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesToWatchServiceConfiguration.moviesToWatchService;
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
		assertThat(thrown).isInstanceOf(UserDoesNotExistException.class);
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
	void user_should_be_able_to_add_movies_with_same_title() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final AddMovieToList createdMovie = AddMovieToListSample.builder().userId(userId).build();
		final AddMovieToList movieWithSameTitle = AddMovieToListSample.builder().movieTitle(createdMovie.getMovieTitle()).userId(userId).build();

		// when
		tut.addMovieToList(movieWithSameTitle);
		tut.addMovieToList(movieWithSameTitle);

		// then
		assertThat(tut.getMoviesToWatch(userId)).hasSize(2)
				.extracting(Movie::getTitle)
				.containsExactlyInAnyOrder(createdMovie.getMovieTitle(), movieWithSameTitle.getMovieTitle());
	}

	@Test
	void only_one_duplicated_title_should_be_deleted() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final AddMovieToList parasite = AddMovieToListSample.builder().userId(userId).build();

		final MovieId parasiteId = tut.addMovieToList(parasite);
		final MovieId duplicatedTitleMovieId = tut.addMovieToList(AddMovieToListSample.builder().movieTitle(parasite.getMovieTitle()).userId(userId).build());

		// when
		tut.removeMovieFromList(duplicatedTitleMovieId, userId);

		// then
		assertThat(tut.getMoviesToWatch(userId)).hasSize(1)
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(parasiteId);
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
		assertThat(thrown).isInstanceOf(UserDoesNotExistException.class);
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
		assertThat(thrown).isInstanceOf(MovieDoesNotExistException.class);
	}

	@Test
	void should_throw_exception_when_trying_to_move_the_movie_when_movie_id_doesnt_exist() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		// when
		final Throwable thrown = catchThrowable(() -> tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(MovieDoesNotExistException.class);
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
		assertThat(thrown).isInstanceOf(MovieDoesNotExistException.class);
	}

}
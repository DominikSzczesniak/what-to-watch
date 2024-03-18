package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.queries.GetMoviesToWatchSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.queries.GetWatchedMoviesToWatchSample;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedMovies;
import pl.szczesniak.dominik.whattowatch.movies.query.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesFacadeConfiguration.moviesFacade;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class MoviesFacadeListTest {

	private InMemoryUserProvider userProvider;

	private MoviesFacade tut;

	@BeforeEach
	void setUp() {
		userProvider = new InMemoryUserProvider();
		tut = moviesFacade(userProvider);
	}

	@Test
	void user_should_add_movie_to_his_list() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());
		final PagedMovies moviesToWatch1 = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());
		assertThat(moviesToWatch1.getMovies()).hasSize(0);

		// when
		tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());

		// then
		final PagedMovies moviesToWatch2 = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());
		assertThat(moviesToWatch2.getMovies()).hasSize(1);
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
		final PagedMovies moviesToWatch = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());
		assertThat(moviesToWatch.getMovies()).hasSize(1)
				.extracting(MovieInListQueryResult::getMovieId)
				.containsExactly(addedMovieId.getValue());
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
		final PagedMovies moviesToWatch = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());
		assertThat(moviesToWatch.getMovies()).hasSize(2)
				.extracting(MovieInListQueryResult::getTitle)
				.containsExactlyInAnyOrder(createdMovie.getMovieTitle().getValue(), movieWithSameTitle.getMovieTitle().getValue());
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
		final PagedMovies moviesToWatch = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());
		assertThat(moviesToWatch.getMovies()).hasSize(1)
				.extracting(MovieInListQueryResult::getMovieId)
				.containsExactlyInAnyOrder(movieId.getValue());
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
		final PagedMovies userOneMovies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userIdOne).build());
		assertThat(userOneMovies.getMovies())
				.extracting(MovieInListQueryResult::getMovieId)
				.containsExactlyInAnyOrder(firstUserMovieOne.getValue(), firstUserMovieTwo.getValue());

		final PagedMovies userTwoMovies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userIdTwo).build());
		assertThat(userTwoMovies.getMovies())
				.extracting(MovieInListQueryResult::getMovieId)
				.containsExactlyInAnyOrder(secondUserMovie.getValue());
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
		final PagedMovies userOneMovies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userIdOne).build());
		final PagedMovies userTwoMovies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userIdTwo).build());

		assertThat(userOneMovies.getMovies()).hasSize(2);
		assertThat(userTwoMovies.getMovies()).hasSize(1);
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
		final PagedMovies userOneMovies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userIdOne).build());
		final PagedMovies userTwoMovies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userIdTwo).build());

		assertThat(userOneMovies.getMovies())
				.extracting(MovieInListQueryResult::getMovieId)
				.containsExactlyInAnyOrder(firstUserMovie.getValue());

		assertThat(userTwoMovies.getMovies())
				.extracting(MovieInListQueryResult::getMovieId)
				.containsExactlyInAnyOrder(secondUserMovie.getValue());
	}

	@Test
	void list_should_be_empty_when_no_movie_added() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		// when
		final PagedMovies movies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());

		// then
		assertThat(movies.getMovies().isEmpty()).isTrue();
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
		final PagedMovies movies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());

		assertThat(movies.getMovies())
				.extracting(MovieInListQueryResult::getMovieId, MovieInListQueryResult::getTitle)
				.containsExactlyInAnyOrder(
						tuple(movieOneId.getValue(), movieOne.getMovieTitle().getValue()),
						tuple(movieThreeId.getValue(), movieThree.getMovieTitle().getValue())
				);

		assertThat(tut.getWatchedMovies(GetWatchedMoviesToWatchSample.builder().userId(userId).build()).getMovies())
				.extracting(WatchedMovieQueryResult::getMovieId, WatchedMovieQueryResult::getTitle)
				.containsExactlyInAnyOrder(tuple(movieTwoId.getValue(), movieTwo.getMovieTitle().getValue()));
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
		final PagedMovies movies = tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build());
		assertThat(movies.getMovies())
				.extracting(MovieInListQueryResult::getTitle)
				.containsExactly(changedTitle.getValue());
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
	void should_delete_all_user_movies() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());
		tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());
		tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());

		// when
		tut.handleUserDeleted(userId);

		// then
		assertThat(tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build()).getMovies()).hasSize(0);
	}

	@Test
	void should_delete_all_user_watched_movies() {
		// given
		final UserId userId = userProvider.addUser(createAnyUserId());

		final MovieId movie_1 = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());
		final MovieId movie_2 = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());
		final MovieId movie_3 = tut.addMovieToList(AddMovieToListSample.builder().userId(userId).build());

		tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().userId(userId).movieId(movie_1).build());
		tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().userId(userId).movieId(movie_2).build());
		tut.moveMovieToWatchedList(MoveMovieToWatchListSample.builder().userId(userId).movieId(movie_3).build());

		// when
		tut.handleUserDeleted(userId);

		// then
		assertThat(tut.getMoviesToWatch(GetMoviesToWatchSample.builder().userId(userId).build()).getMovies()).hasSize(0);
	}

}

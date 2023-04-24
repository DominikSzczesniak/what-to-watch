package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesToWatchServiceConfiguration.moviesToWatchService;

class MoviesToWatchServiceTest {

	private InMemoryUserProvider userProvider;
	private MoviesToWatchService tut;

	@BeforeEach
	void setUp() {
		userProvider = new InMemoryUserProvider();
		tut = moviesToWatchService(userProvider);
	}

	@Test
	void user_should_add_movie_to_his_list() {
		// given
		final UserId userId = new UserId(3);
		userProvider.addUser(userId);
		assertThat(tut.getList(userId)).hasSize(0);

		// when
		tut.addMovieToList(new MovieTitle("Parasite"), userId);

		// then
		assertThat(tut.getList(userId)).hasSize(1);
	}

	@Test
	void should_not_add_movie_when_user_doesnt_exist() {
		// given
		final UserId userId = new UserId(4);

		// when
		final Throwable thrown = catchThrowable(() -> tut.addMovieToList(new MovieTitle("Parasite"), userId));

		// then
		assertThat(thrown).isInstanceOf(UserDoesNotExistException.class);
	}

	@Test
	void user_should_be_able_to_add_movies_with_same_title() {
		// given
		final UserId userId = new UserId(51);
		userProvider.addUser(userId);

		// when
		tut.addMovieToList(new MovieTitle("Parasite"), userId);
		tut.addMovieToList(new MovieTitle("Star Wars"), userId);
		tut.addMovieToList(new MovieTitle("Viking"), userId);
		tut.addMovieToList(new MovieTitle("Viking"), userId);

		// then
		assertThat(tut.getList(userId)).hasSize(4)
				.extracting(movie -> movie.getTitle().getValue())
				.containsExactlyInAnyOrder("Parasite", "Star Wars", "Viking", "Viking");
	}

	@Test
	void user_should_delete_movie_from_his_list() {
		// given
		final UserId userId = new UserId(3);
		userProvider.addUser(userId);

		final MovieId movieToRemove = tut.addMovieToList(new MovieTitle("Parasite"), userId);
		tut.addMovieToList(new MovieTitle("Star Wars"), userId);
		tut.addMovieToList(new MovieTitle("Viking"), userId);

		// when
		tut.removeMovieFromList(movieToRemove, userId);

		// then
		assertThat(tut.getList(userId)).hasSize(2)
				.extracting(movie -> movie.getTitle().getValue())
				.containsExactlyInAnyOrder("Star Wars", "Viking");
	}

	@Test
	void only_one_duplicated_title_should_be_deleted() {
		// given
		final UserId userId = new UserId(9);
		userProvider.addUser(userId);

		final MovieId parasiteId = tut.addMovieToList(new MovieTitle("Parasite"), userId);
		final MovieId starWarsId = tut.addMovieToList(new MovieTitle("Star Wars"), userId);
		final MovieId vikingId = tut.addMovieToList(new MovieTitle("Viking"), userId);

		final MovieId movieIdToRemove = tut.addMovieToList(new MovieTitle("Viking"), userId);

		// when
		tut.removeMovieFromList(movieIdToRemove, userId);

		// then
		assertThat(tut.getList(userId)).hasSize(3)
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(parasiteId, starWarsId, vikingId);
	}

	@Test
	void two_different_users_should_have_different_lists() {
		// given
		final UserId userIdOne = new UserId(1);
		final UserId userIdTwo = new UserId(2);
		userProvider.addUser(userIdOne);
		userProvider.addUser(userIdTwo);


		// when
		final MovieId firstUserMovieOne = tut.addMovieToList(new MovieTitle("Parasite"), userIdOne);
		final MovieId secondUserMovie = tut.addMovieToList(new MovieTitle("Parasite"), userIdTwo);
		final MovieId firstUserMovieTwo = tut.addMovieToList(new MovieTitle("Viking"), userIdOne);

		// then
		assertThat(tut.getList(userIdOne))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(firstUserMovieOne, firstUserMovieTwo);

		assertThat(tut.getList(userIdTwo))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(secondUserMovie);
	}

	@Test
	void should_not_delete_movie_if_not_users_movie() {
		// given
		final UserId userIdOne = new UserId(32);
		final UserId userIdTwo = new UserId(21);
		userProvider.addUser(userIdOne);
		userProvider.addUser(userIdTwo);

		tut.addMovieToList(new MovieTitle("Parasite"), userIdOne);
		tut.addMovieToList(new MovieTitle("Viking"), userIdOne);
		final MovieId movieToDelete = tut.addMovieToList(new MovieTitle("Parasite"), userIdTwo);
		// when


		tut.removeMovieFromList(movieToDelete, userIdOne);

		// then
		assertThat(tut.getList(userIdOne)).hasSize(2);
		assertThat(tut.getList(userIdTwo)).hasSize(1);
	}

	@Test
	void should_delete_movie_only_from_this_user_list_and_not_all_of_them() {
		// given
		final UserId userIdOne = new UserId(413);
		final UserId userIdTwo = new UserId(22);
		userProvider.addUser(userIdOne);
		userProvider.addUser(userIdTwo);

		final MovieId movieToDelete = tut.addMovieToList(new MovieTitle("Parasite"), userIdOne);
		final MovieId secondUserMovie = tut.addMovieToList(new MovieTitle("Parasite"), userIdTwo);
		final MovieId firstUserMovie = tut.addMovieToList(new MovieTitle("Viking"), userIdOne);
		// when

		tut.removeMovieFromList(movieToDelete, userIdOne);

		// then
		assertThat(tut.getList(userIdOne))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(firstUserMovie);

		assertThat(tut.getList(userIdTwo))
				.extracting(Movie::getMovieId)
				.containsExactlyInAnyOrder(secondUserMovie);
	}

	@Test
	void list_should_be_empty_when_no_movie_added() {
		// given
		final UserId userId = new UserId(68);
		userProvider.addUser(userId);

		// when
		final List<Movie> movies = tut.getList(userId);

		// then
		assertThat(movies.isEmpty()).isTrue();
	}

}
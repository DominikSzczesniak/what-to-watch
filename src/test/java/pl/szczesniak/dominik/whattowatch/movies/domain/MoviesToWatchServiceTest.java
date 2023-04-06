package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
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
        final UserId userId = new UserId(1);
        userProvider.addUser(userId);

        // when
        tut.addMovieToList("Parasite", userId);

        // then
        assertThat(tut.getList(userId)).hasSize(1);
    }

    @Test
    void shouldnt_add_movie_when_user_doesnt_exist() {
        // given
        final UserId userId = new UserId(1);

        // when
        final Throwable thrown = catchThrowable(() -> tut.addMovieToList("Parasite", userId));

        // then
        assertThat(thrown).isInstanceOf(UserDoesNotExistException.class);
    }

    @Test
    void user_should_be_able_to_add_movies_with_same_title() {
        // given
        final UserId userId = new UserId(1);
        userProvider.addUser(userId);

        // when
        tut.addMovieToList("Parasite", userId);
        tut.addMovieToList("Star Wars", userId);
        tut.addMovieToList("Viking", userId);
        tut.addMovieToList("Viking", userId);

        // then
        assertThat(tut.getList(userId)).hasSize(4);
        assertThat(tut.getList(userId)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Star Wars", "Viking", "Viking");
    }

    @Test
    void user_should_delete_movie_from_his_list() {
        // given
        final UserId userId = new UserId(1);
        userProvider.addUser(userId);

        tut.addMovieToList("Parasite", userId);
        tut.addMovieToList("Star Wars", userId);
        tut.addMovieToList("Viking", userId);

        // when
        tut.removeMovieFromList(new MovieId(1), userId);

        // then
        assertThat(tut.getList(userId)).hasSize(2);
    }

    @Test
    void only_one_duplicated_title_should_be_deleted() {
        // given
        final UserId userId = new UserId(1);
        userProvider.addUser(userId);

        // when
        tut.addMovieToList("Parasite", userId);
        tut.addMovieToList("Star Wars", userId);
        tut.addMovieToList("Viking", userId);
        tut.addMovieToList("Viking", userId);

        tut.removeMovieFromList(new MovieId(3), userId);

        // then
        assertThat(tut.getList(userId)).hasSize(3)
                .extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Star Wars", "Viking");
    }

    @Test
    void two_different_users_should_have_different_lists() {
        // given
        final UserId userIdOne = new UserId(1);
        final UserId userIdTwo = new UserId(2);
        userProvider.addUser(userIdOne);
        userProvider.addUser(userIdTwo);


        // when
        tut.addMovieToList("Parasite", userIdOne);
        tut.addMovieToList("Parasite", userIdTwo);
        tut.addMovieToList("Viking", userIdOne);

        // then
        assertThat(tut.getList(userIdOne)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Viking");
        assertThat(tut.getList(userIdTwo)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }

    @Test
    void should_delete_movie_only_from_this_user_list_and_not_all_of_them() {
        // given
        final UserId userIdOne = new UserId(1);
        final UserId userIdTwo = new UserId(2);
        userProvider.addUser(userIdOne);
        userProvider.addUser(userIdTwo);

        // when
        tut.addMovieToList("Parasite",userIdOne);
        tut.addMovieToList("Parasite", userIdTwo);
        tut.addMovieToList("Viking", userIdOne);

        tut.removeMovieFromList(new MovieId(1), userIdOne);

        // then
        assertThat(tut.getList(userIdOne)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Viking");
        assertThat(tut.getList(userIdTwo)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }

    @Test
    void list_should_be_empty_when_no_movie_added() {
        // given
        final UserId userId = new UserId(1);
        userProvider.addUser(userId);

        // when
        final List<Movie> movies = tut.getList(userId);

        // then
        assertThat(movies.isEmpty()).isTrue();
    }

}
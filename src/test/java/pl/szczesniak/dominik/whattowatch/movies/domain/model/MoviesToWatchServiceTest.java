package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TestMoviesToWatchServiceConfiguration.moviesToWatchService;

class MoviesToWatchServiceTest {

    private UserService userService;
    private MoviesToWatchService tut;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        tut = moviesToWatchService(userService);
    }

    @Test
    void user_should_add_movie_to_his_list() {
        // given
        UserId userId = new UserId(1);
        when(userService.exists(userId)).thenReturn(true);

        // when
        tut.addMovieToList("Parasite", userId);

        // then
        assertThat(tut.getList(userId)).hasSize(1);
    }

    @Test
    void user_should_be_able_to_add_movies_with_same_title() {
        // given
        UserId userId = new UserId(1);
        when(userService.exists(userId)).thenReturn(true);

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
        UserId userId = new UserId(1);
        when(userService.exists(userId)).thenReturn(true);

        // when
        tut.addMovieToList("Parasite", userId);
        tut.addMovieToList("Star Wars", userId);
        tut.addMovieToList("Viking", userId);

        tut.removeMovieFromList("Parasite", userId);

        // then
        assertThat(tut.getList(userId)).hasSize(2);
    }

    @Test
    void two_different_users_should_have_different_lists() {
        // given
        UserId userIdOne = new UserId(1);
        UserId userIdTwo = new UserId(2);
        when(userService.exists(userIdOne)).thenReturn(true);
        when(userService.exists(userIdTwo)).thenReturn(true);

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
        UserId userIdOne = new UserId(1);
        UserId userIdTwo = new UserId(2);
        when(userService.exists(userIdOne)).thenReturn(true);
        when(userService.exists(userIdTwo)).thenReturn(true);

        // when
        tut.addMovieToList("Parasite",userIdOne);
        tut.addMovieToList("Parasite", userIdTwo);
        tut.addMovieToList("Viking", userIdOne);

        tut.removeMovieFromList("Parasite", userIdOne);

        // then
        assertThat(tut.getList(userIdOne)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Viking");
        assertThat(tut.getList(userIdTwo)).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }

    @Test
    void list_should_be_empty_when_no_movie_added() {
        // given
        UserId userId = new UserId(1);
        when(userService.exists(userId)).thenReturn(true);

        // when
        final List<Movie> movies = tut.getList(userId);

        // then
        assertThat(movies.isEmpty()).isTrue();
    }

}
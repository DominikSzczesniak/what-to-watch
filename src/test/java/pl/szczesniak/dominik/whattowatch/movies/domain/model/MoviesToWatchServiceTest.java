package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InMemoryUserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TestMoviesToWatchServiceConfiguration.moviesToWatchService;

class MoviesToWatchServiceTest {

//    MoviesToWatchService mocked = mock();
    private final UserService userService = new UserServiceConfiguration().userService(new InMemoryUserRepository());
    private final MoviesToWatchService tut = moviesToWatchService(userService);

    @Test
    void user_should_add_movie_to_his_list() {
        // given
        userService.createUser("Dominik");

//        when(mocked.getList(userService.getUserId("Dominik")));

        // when
        tut.addMovieToList("Parasite", userService.getUserId("Dominik"));

        // then
        assertThat(tut.getList(userService.getUserId("Dominik"))).hasSize(1);
    }

    @Test
    void should_ignore_duplicated_movies() {
        // given
        userService.createUser("Dominik");

        // when
        tut.addMovieToList("Parasite", userService.getUserId("Dominik"));
        tut.addMovieToList("Star Wars", userService.getUserId("Dominik"));
        tut.addMovieToList("Viking", userService.getUserId("Dominik"));
        tut.addMovieToList("Viking", userService.getUserId("Dominik"));

        // then
        assertThat(tut.getList(userService.getUserId("Dominik"))).hasSize(3);
        assertThat(tut.getList(userService.getUserId("Dominik"))).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Star Wars", "Viking");
    }

    @Test
    void user_should_delete_movie_from_his_list() {
        // given
        userService.createUser("Dominik");

        // when
        tut.addMovieToList("Parasite", userService.getUserId("Dominik"));
        tut.addMovieToList("Star Wars", userService.getUserId("Dominik"));
        tut.addMovieToList("Viking", userService.getUserId("Dominik"));

        tut.removeMovieFromList(userService.getUserId("Dominik"), "Parasite");

        // then
        assertThat(tut.getList(userService.getUserId("Dominik"))).hasSize(2);
    }

    @Test
    void two_different_users_should_have_different_lists() {
        // given
        userService.createUser("Dominik");
        userService.createUser("Patryk");

        // when
        tut.addMovieToList("Parasite", userService.getUserId("Dominik"));
        tut.addMovieToList("Parasite", userService.getUserId("Patryk"));
        tut.addMovieToList("Viking", userService.getUserId("Dominik"));

        // then
        assertThat(tut.getList(userService.getUserId("Dominik"))).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Viking");
        assertThat(tut.getList(userService.getUserId("Patryk"))).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }

    @Test
    void should_delete_movie_only_from_userOne_list() {
        // given
        userService.createUser("Dominik");
        userService.createUser("Patryk");

        // when
        tut.addMovieToList("Parasite", userService.getUserId("Dominik"));
        tut.addMovieToList("Parasite", userService.getUserId("Patryk"));
        tut.addMovieToList("Viking", userService.getUserId("Dominik"));
        tut.removeMovieFromList(userService.getUserId("Dominik"), "Parasite");

        // then
        assertThat(tut.getList(userService.getUserId("Dominik"))).extracting(Movie::getTitle).containsExactlyInAnyOrder("Viking");
        assertThat(tut.getList(userService.getUserId("Patryk"))).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }

    @Test
    void list_should_be_empty() {
        // given
        userService.createUser("Dominik");

        // when
        final List<Movie> movies = tut.getList(userService.getUserId("Dominik"));

        // then
        assertThat(movies.isEmpty()).isTrue();
    }

    @Test
    void mockito_test() {
        // given
        userService.createUser("Dominik");
        when(userService.getUserId("Dominik")).thenReturn(new UserId(1));

        // when

        // then
    }
}
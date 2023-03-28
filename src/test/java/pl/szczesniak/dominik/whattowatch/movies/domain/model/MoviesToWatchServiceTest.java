package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TestMoviesToWatchServiceConfiguration.moviesToWatchService;

class MoviesToWatchServiceTest {

    final MoviesToWatchService tut = moviesToWatchService();

    @Test
    void list_should_be_empty() {
        // given
        final User user = new User("Dominik");

        // when
        final List<Movie> movies = tut.getList(user.getId());

        // then
        assertThat(movies.isEmpty()).isTrue();
    }

    @Test
    void user_should_add_movie_to_his_list() {
        // given
        final User userOne = new User("Dominik");

        // when
        tut.addMovieToList(userOne, "Parasite", userOne.getId());

        // then
        assertThat(tut.getList(userOne.getId())).hasSize(1);
    }

    @Test
    void should_ignore_duplicated_movies() {
        // given
        final User userOne = new User("Dominik");

        // when
        tut.addMovieToList(userOne, "Parasite", userOne.getId());
        tut.addMovieToList(userOne, "Star Wars", userOne.getId());
        tut.addMovieToList(userOne, "Viking", userOne.getId());
        tut.addMovieToList(userOne, "Viking", userOne.getId());

        // then
        assertThat(tut.getList(userOne.getId())).hasSize(3);
        assertThat(tut.getList(userOne.getId())).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Star Wars", "Viking");
    }

    @Test
    void user_should_delete_movie_from_his_list() {
        // given
        final User userOne = new User("Dominik");

        // when
        tut.addMovieToList(userOne, "Parasite", userOne.getId());
        tut.addMovieToList(userOne, "Star Wars", userOne.getId());
        tut.addMovieToList(userOne, "Viking", userOne.getId());

        tut.removeMovieFromList(userOne.getId(), "Parasite");

        // then
        assertThat(tut.getList(userOne.getId())).hasSize(2);
    }

    @Test
    void two_different_users_should_have_different_lists() {
        // given
        final User userOne = new User("Dominik");
        final User userTwo = new User("Patryk");

        // when
        tut.addMovieToList(userOne, "Parasite", userOne.getId());
        tut.addMovieToList(userTwo, "Parasite", userTwo.getId());
        tut.addMovieToList(userOne, "Viking", userOne.getId());

        // then
        assertThat(tut.getList(userOne.getId())).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite", "Viking");
        assertThat(tut.getList(userTwo.getId())).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }

    @Test
    void should_delete_movie_only_from_userOne_list() {
        // given
        final User userOne = new User("Dominik");
        final User userTwo = new User("Patryk");

        // when
        tut.addMovieToList(userOne, "Parasite", userOne.getId());
        tut.addMovieToList(userTwo, "Parasite", userTwo.getId());
        tut.addMovieToList(userOne, "Viking", userOne.getId());
        tut.removeMovieFromList(userOne.getId(), "Parasite");

        // then
        assertThat(tut.getList(userOne.getId())).extracting(Movie::getTitle).containsExactlyInAnyOrder("Viking");
        assertThat(tut.getList(userTwo.getId())).extracting(Movie::getTitle).containsExactlyInAnyOrder("Parasite");
    }
}
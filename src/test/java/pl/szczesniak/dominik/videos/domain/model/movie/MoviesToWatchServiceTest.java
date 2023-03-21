package pl.szczesniak.dominik.videos.domain.model.movie;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.videos.domain.exceptions.MovieIsNotOnTheListException;
import pl.szczesniak.dominik.videos.domain.model.User;
import pl.szczesniak.dominik.videos.domain.model.movie.Movie;
import pl.szczesniak.dominik.videos.domain.model.movie.MoviesToWatchService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

class MoviesToWatchServiceTest {

    MoviesToWatchService tut = new MoviesToWatchService();

    @Test
    void list_should_be_empty() {
        // given
        User user = new User();
        // when
        ArrayList<Movie> movies = tut.getList(user);
        // then
        assertThat(movies.isEmpty()).isTrue();
    }

    @Test
    void user_should_add_movie_to_his_list() {
        // given
        User userOne = new User();
        Movie parasite = new Movie("Parasite");

        // when
        tut.addMovieToList(userOne, parasite);

        // then
        assertThat(tut.getList(userOne)).contains(parasite);
    }

    @Test
    void user_should_not_be_able_to_add_duplicates() {
        // given
        User userOne = new User();
        Movie parasite = new Movie("Parasite");
        Movie starWars = new Movie("Star Wars");

        // when
        tut.addMovieToList(userOne, parasite);
        tut.addMovieToList(userOne, starWars);
        tut.addMovieToList(userOne, parasite);

        // then
        assertThat(tut.getList(userOne)).containsExactly(parasite, starWars);
    }

    @Test
    void user_should_delete_movie_from_his_list() {
        // given
        User userOne = new User();
        Movie parasite = new Movie("Parasite");
        Movie starWars = new Movie("Star Wars");
        tut.addMovieToList(userOne, parasite);
        tut.addMovieToList(userOne, starWars);

        // when
        tut.removeMovieFromList(userOne, parasite);

        // then
        assertThat(tut.getList(userOne)).containsExactly(starWars);
    }

    @Test
    void user_should_not_be_able_to_delete_movie_if_its_not_in_the_list() {
        // given
        User userOne = new User();
        Movie parasite = new Movie("Parasite");
        Movie starWars = new Movie("Star Wars");
        Movie lordOfTheRings = new Movie("Lord of the Rings");

        // when
        tut.addMovieToList(userOne, parasite);
        tut.addMovieToList(userOne, starWars);
        Throwable thrown = catchThrowable(() -> tut.removeMovieFromList(userOne, lordOfTheRings));

        // then
        assertThat(thrown).isInstanceOf(MovieIsNotOnTheListException.class);
    }

    @Test
    void two_different_users_should_have_different_lists() {
        // given
        final User userOne = new User();
        final User userTwo = new User();
        Movie amsterdam = new Movie("Amsterdam");
        Movie berlin = new Movie("Berlin");
        Movie warszawa = new Movie("Warszawa");
        Movie tokio = new Movie("Tokio");
        // when
        tut.addMovieToList(userOne, amsterdam);
        tut.addMovieToList(userOne, warszawa);
        tut.addMovieToList(userTwo, berlin);
        tut.addMovieToList(userOne, tokio);

        // then
        assertThat(tut.getList(userOne)).containsExactly(amsterdam, warszawa, tokio);
        assertThat(tut.getList(userTwo)).containsExactly(berlin);
    }
}
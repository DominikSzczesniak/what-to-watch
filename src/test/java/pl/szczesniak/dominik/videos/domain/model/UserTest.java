package pl.szczesniak.dominik.videos.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.videos.domain.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void two_users_should_have_different_id() {
        // when
        User userOne = new User();
        User userTwo = new User();

        // then
        assertThat(userOne.getId()).isNotEqualTo(userTwo.getId());
    }
}
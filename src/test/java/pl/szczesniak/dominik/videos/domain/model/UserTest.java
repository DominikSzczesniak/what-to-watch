package pl.szczesniak.dominik.videos.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void two_users_should_have_different_id() {
        // when
        final User userOne = new User();
        final User userTwo = new User();

        // then
        assertThat(userOne.getId()).isNotEqualTo(userTwo.getId());
    }
}
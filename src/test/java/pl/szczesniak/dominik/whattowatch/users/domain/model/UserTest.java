package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void two_users_should_have_different_id() {
        // when
        final User userOne = new User("Dominik");
        final User userTwo = new User("Patryk");

        // then
        assertThat(userOne.getId()).isNotEqualTo(userTwo.getId());
    }
}
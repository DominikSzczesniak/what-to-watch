package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.infrastructure.persistence.InFileUsersRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserService tut = new UserService(new InFileUsersRepository("test.csv"));

    @Test
    void should_save_player() {
        // given
        User user = new User("Dominik");

        // when
        tut.saveUser(user);

        // then
        assertThat(tut.getUserId(user)).isEqualTo(2);
    }
}
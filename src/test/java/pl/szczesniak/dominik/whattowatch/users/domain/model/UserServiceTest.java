package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUsersRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserService tut = new UserService(new InFileUsersRepository("test.csv"));

    @Test
    void should_return_userId_2() {
        // given
        tut.createUser("Grzegorz");

        // when
        int id = tut.getUserId("Grzegorz");

        // then
        assertThat(id).isEqualTo(2);
    }
}
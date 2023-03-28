package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceInFileTest {

    UserService tut = new UserService(new InFileUserRepository("test.csv"));

    @Test
    void should_return_userId_2() {
        // given
        tut.createUser("Grzegorz");

        // when
        int id = tut.getUserId("Grzegorz").getId();

        // then
        assertThat(id).isEqualTo(2);
    }
}
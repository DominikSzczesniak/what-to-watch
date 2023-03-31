package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceInFileTest {

    UserService tut = new UserService(new InFileUserRepository("test.csv"));

    @Test
    void should_return_correct_user_id() {
        // when
        final UserId idOne = tut.createUser("Grzegorz");
        final UserId idTwo = tut.createUser("Maciej");
        final UserId idThree = tut.createUser("Anna");

        // then
        assertThat(idOne.getId()).isEqualTo(1);
        assertThat(idTwo.getId()).isEqualTo(2);
        assertThat(idThree.getId()).isEqualTo(3);
    }
}
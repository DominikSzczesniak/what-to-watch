package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InMemoryUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private UserService tut;

    @BeforeEach
    void setUp() {
        tut = new UserService(new InMemoryUserRepository());
    }

    @Test
    void should_return_true_when_user_created() {
        // when
        final UserId id = tut.createUser("Dominik");

        // then
        assertThat(tut.exists(id)).isTrue();
    }

    @Test
    void should_return_1_when_creating_one_user() {
        // when
        final UserId id = tut.createUser("Kamil");

        // then
        assertThat(id.getId()).isEqualTo(1);
    }

    @Test
    void created_users_should_have_different_ids() {
        // when
        final UserId idOne = tut.createUser("Dominik");
        final UserId idTwo = tut.createUser("Patryk");

        // then
        assertThat(idOne.getId()).isNotEqualTo(idTwo.getId());
    }

//    @Test
//    void should_return_correct_user_id() {
//        // when
//        final UserId idOne = tut.createUser("Grzegorz");
//        final UserId idTwo = tut.createUser("Maciej");
//
//        // then
//        assertThat(idOne.getId()).isEqualTo(2);
//        assertThat(idTwo.getId()).isEqualTo(5);
//    }
}
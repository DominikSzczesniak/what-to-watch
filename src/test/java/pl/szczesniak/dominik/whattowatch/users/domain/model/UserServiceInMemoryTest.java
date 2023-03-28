package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InMemoryUserRepository;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.TestUserServiceConfiguration.userService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;



class UserServiceInMemoryTest {

    private final UserService tut = userService(new InMemoryUserRepository());

    @Test
    void should_return_true_when_user_exists() {
        // when
        tut.createUser("Dominik");

        // then
        assertThat(tut.exists(tut.getUserId("Dominik"))).isTrue();
    }

    @Test
    void should_throw_exception_user_does_not_exist() {
        // when
        final Throwable thrown = catchThrowable(() -> tut.exists(tut.getUserId("Dominik")));

        // then
        assertThat(thrown).isInstanceOf(UserDoesNotExistException.class);
    }

    @Test
    void should_return_1_when_creating_one_user() {
        // when
        tut.createUser("Dominik");

        // then
        assertThat(tut.getUserId("Dominik").getId()).isEqualTo(1);
    }

    @Test
    void users_should_have_different_ids() {
        // when
        tut.createUser("Dominik");
        tut.createUser("Patryk");

        // then
        assertThat(tut.getUserId("Dominik").getId()).isNotEqualTo(tut.getUserId("Patryk").getId());
    }
}
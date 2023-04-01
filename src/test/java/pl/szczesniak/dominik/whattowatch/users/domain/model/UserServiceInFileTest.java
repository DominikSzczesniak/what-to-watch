package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUserRepository;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceInFileTest {

    @TempDir
    File testFile = new File("temporaryfile.txt");

    UserService tut = new UserService(new InFileUserRepository(testFile.getName()));

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
        testFile.deleteOnExit();
    }
}
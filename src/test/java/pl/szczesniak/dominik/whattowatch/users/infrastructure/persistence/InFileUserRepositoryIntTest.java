package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class InFileUserRepositoryIntTest {

	@TempDir
	File testFile = new File("intTemporaryFile.txt");

	private InFileUserRepository tut;

	@BeforeEach
	void setUp() {
		tut = new InFileUserRepository(testFile.getAbsolutePath() + testFile.getName());
	}

	@Test
	void should_find_all_previously_written_to_file() {
		// given
		tut = new InFileUserRepository("src/test/resources/users.csv");

		// when
		final List<User> all = tut.findAll();

		// then
		assertThat(all).extracting(User::getUserId).containsExactlyInAnyOrder(new UserId(1), new UserId(2), new UserId(3));
		assertThat(all).extracting(User::getUserId).extracting(UserId::getValue).containsExactlyInAnyOrder(1, 2, 3);
		assertThat(all).extracting(User::getUserName).containsExactlyInAnyOrder("Dominik", "Patryk", "Michal");
	}

	@Test
	void should_return_next_id_based_on_provided_file() {
		// given
		tut = new InFileUserRepository("src/test/resources/users.csv");

		// then
		assertThat(tut.nextUserId().getValue()).isEqualTo(4);
	}

	@Test
	void next_id_should_be_one_higher_than_number_of_already_created_users() {
		// when
		tut.saveUser(new User("Anna", tut.nextUserId()));
		tut.saveUser(new User("Agata", tut.nextUserId()));
		tut.saveUser(new User("Magdalena", tut.nextUserId()));

		// then
		assertThat(tut.nextUserId().getValue()).isEqualTo(4);

//		UserId idOne = tut.nextUserId();
//		UserId idTwo = tut.nextUserId();
//		UserId idThree = tut.nextUserId();
//
//		// then
//		assertThat(idOne.getValue()).isEqualTo(1);
//		assertThat(idTwo.getValue()).isEqualTo(2);
//		assertThat(idThree.getValue()).isEqualTo(3);
	}

	@Test
	void should_find_all_created_users() {
		// when
		tut.saveUser(new User("Anna", tut.nextUserId()));
		tut.saveUser(new User("Agata", tut.nextUserId()));
		tut.saveUser(new User("Magdalena", tut.nextUserId()));

		// then
		assertThat(tut.findAll()).extracting(User::getUserName).containsExactlyInAnyOrder("Anna", "Agata", "Magdalena");
	}

	@Test
	void should_throw_exception_when_trying_to_create_user_that_already_exists() {
		// given
		tut.saveUser(new User("Anna", new UserId(1)));

		// when
		Throwable thrown = catchThrowable(() -> tut.saveUser(new User("Michal", new UserId(1))));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}
}
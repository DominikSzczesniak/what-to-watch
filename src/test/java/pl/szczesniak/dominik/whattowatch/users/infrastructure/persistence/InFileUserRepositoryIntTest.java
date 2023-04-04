package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;

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
		tut = new InFileUserRepository("C:\\Users\\sarat\\IdeaProjects\\what-to-watch\\src\\test\\resources\\users.csv");

		// when
		final List<User> all = tut.findAll();

		// then
		assertThat(all).containsExactly(
				new User("Dominik", new UserId(1)),
				new User("Patryk", new UserId(2)),
				new User("Michal", new UserId(3)));
	}

	@Test
	void should_return_next_id_based_on_provided_file() {
		// given
		tut = new InFileUserRepository("C:\\Users\\sarat\\IdeaProjects\\what-to-watch\\src\\test\\resources\\users.csv");

		// then
		assertThat(tut.nextUserId().getValue()).isEqualTo(4);
	}

	@Test
	void creating_user_should_assign_correct_id() {
		// when
		tut.createUser(new User("Anna", tut.nextUserId()));
		tut.createUser(new User("Dominik", tut.nextUserId()));

		// then
		assertThat(tut.findAll()).containsExactlyInAnyOrder(
				new User("Anna", new UserId(1)),
				new User("Dominik", new UserId(2)));
	}

	@Test
	void next_id_should_be_one_higher_than_number_of_already_created_users() {
		// when
		tut.createUser(new User("Anna", tut.nextUserId()));
		tut.createUser(new User("Agata", tut.nextUserId()));
		tut.createUser(new User("Magdalena", tut.nextUserId()));

		// then
		assertThat(tut.nextUserId().getValue()).isEqualTo(4);
	}

	@Test
	void should_find_all_created_users() {
		// when
		tut.createUser(new User("Anna", tut.nextUserId()));
		tut.createUser(new User("Agata", tut.nextUserId()));
		tut.createUser(new User("Magdalena", tut.nextUserId()));

		// then
		assertThat(tut.findAll()).containsExactly(
				new User("Anna", new UserId(1)),
				new User("Agata", new UserId(2)),
				new User("Magdalena", new UserId(3))
		);
	}

	@Test
	void should_throw_exception_when_trying_to_create_user_that_already_exists() {
		// given
		tut.createUser(new User("Anna", new UserId(1)));

		// when
		Throwable thrown = catchThrowable(() -> tut.createUser(new User("Anna", new UserId(1))));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}
}
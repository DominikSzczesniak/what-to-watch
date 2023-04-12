package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class InFileUserRepositoryIntTest {

	@TempDir
	private File testFileUsers = new File("intTemporaryFileUsers.txt");
	@TempDir
	private File testFileId = new File("intTemporaryFileId.txt");

	private String existingUsersDbFilepath;
	private String existingUsersIdDbFilepath;
	private InFileUserRepository tut;

	@BeforeEach
	void setUp() {
		existingUsersDbFilepath = "src/test/resources/users.csv";
		existingUsersIdDbFilepath = "src/test/resources/usersId.csv";
		tut = new InFileUserRepository(testFileUsers.getAbsolutePath() + testFileUsers.getName(),
				testFileId.getAbsolutePath() + testFileId.getName());
	}

	@Test
	void should_find_by_name_all_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		User dominik = tut.findBy("Dominik").get();
		User patryk = tut.findBy("Patryk").get();
		User michal = tut.findBy("Michal").get();

		// then
		assertThat(tut.exists(dominik.getUserId())).isTrue();
		assertThat(tut.exists(patryk.getUserId())).isTrue();
		assertThat(tut.exists(michal.getUserId())).isTrue();
	}

	@Test
	void should_find_by_id_all_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		Optional<User> dominik = tut.findBy(new UserId(1));
		Optional<User> patryk = tut.findBy(new UserId(2));
		Optional<User> michal = tut.findBy(new UserId(3));

		// then
		assertThat(tut.exists(dominik.get().getUserId())).isTrue();
		assertThat(tut.exists(patryk.get().getUserId())).isTrue();
		assertThat(tut.exists(michal.get().getUserId())).isTrue();
	}

	@Test
	void should_return_empty_when_no_user_found() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		Optional<User> nonExistingId = tut.findBy(new UserId(5));
		Optional<User> nonExistingName = tut.findBy("Kamil");

		// then
		assertThat(nonExistingId.isPresent()).isFalse();
		assertThat(nonExistingName.isPresent()).isFalse();
	}

	@Test
	void should_return_4_when_asked_for_next_user_id_from_given_file() {
		// when
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// then
		assertThat(tut.findNextUserId()).isEqualTo(new UserId(4));
	}

	@Test
	void next_id_should_be_one_higher_than_number_of_already_created_userIds() {
		// when
		UserId idOne = tut.nextUserId();
		UserId idTwo = tut.nextUserId();
		UserId idThree = tut.nextUserId();

		// then
		assertThat(tut.nextUserId().getValue()).isEqualTo(4);
	}

	@Test
	void should_throw_exception_when_creating_user_with_already_existing_userid() {
		// given
		tut.create(new User(new Username("Anna"), new UserId(1)));

		// when
		Throwable thrown = catchThrowable(() -> tut.create(new User(new Username("Michal"), new UserId(1))));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}

}
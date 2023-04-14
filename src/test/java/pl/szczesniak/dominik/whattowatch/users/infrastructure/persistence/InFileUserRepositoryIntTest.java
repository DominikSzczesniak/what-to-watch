package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions.UsernameIsTakenException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;

class InFileUserRepositoryIntTest {

	@TempDir
	private File testFileUsers = new File("intTemporaryFileUsers.txt");
	@TempDir
	private File testFileId = new File("intTemporaryFileId.txt");

	private String existingUsersDbFilepath;
	private String existingUsersIdDbFilepath;
	private InFileUserRepository tut;
	private final UserPassword password = new UserPassword("asd");

	@BeforeEach
	void setUp() {
		existingUsersDbFilepath = "src/test/resources/users.csv";
		existingUsersIdDbFilepath = "src/test/resources/usersId.csv";
		tut = new InFileUserRepository(testFileUsers.getAbsolutePath() + testFileUsers.getName(),
				testFileId.getAbsolutePath() + testFileId.getName());
	}

	@Test
	void should_return_empty_when_no_user_found_in_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		Optional<User> nonExistingId = tut.findBy(new UserId(5));
		Optional<User> nonExistingName = tut.findBy("Kamil");

		// then
		assertThat(nonExistingId.isEmpty()).isTrue();
		assertThat(nonExistingName.isEmpty()).isTrue();
	}

	@Test
	void should_find_by_name_all_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		Optional<User> dominik = tut.findBy("Dominik");
		Optional<User> patryk = tut.findBy("Patryk");
		Optional<User> michal = tut.findBy("Michal");
		List<User> users = List.of(dominik.get(), patryk.get(), michal.get());

		// then
		assertThat(users).hasSize(3)
				.extracting(User::getUserId, User::getUserName)
				.contains(
						tuple(new UserId(1), new Username("Dominik")),
						tuple(new UserId(2), new Username("Patryk")),
						tuple(new UserId(3), new Username("Michal"))
				);
	}

	@Test
	void should_find_by_id_all_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		Optional<User> dominik = tut.findBy(new UserId(1));
		Optional<User> patryk = tut.findBy(new UserId(2));
		Optional<User> michal = tut.findBy(new UserId(3));
		List<User> users = List.of(dominik.get(), patryk.get(), michal.get());

		// then
		assertThat(users).hasSize(3)
				.extracting(User::getUserId, User::getUserName)
				.contains(
						tuple(new UserId(1), new Username("Dominik")),
						tuple(new UserId(2), new Username("Patryk")),
						tuple(new UserId(3), new Username("Michal"))
				);
	}

	@Test
	void should_return_correct_next_user_id_from_given_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		UserId userId = tut.findNextUserId();

		// then
		assertThat(userId).isEqualTo(new UserId(4));
	}

	@Test
	void should_save_correctly_in_file() throws IOException {
		// given
		File user = new File(testFileUsers, "testUser.csv");
		tut = new InFileUserRepository(user.getAbsolutePath(), testFileId.getAbsolutePath() + testFileId.getName());

		// when
		tut.create(new User(new Username("Dominik"), tut.nextUserId(), password));

		// then
		String line = "Dominik,1";
		assertThat(Files.readAllLines(user.toPath())).contains(line);
	}

	@Test
	void should_return_empty_when_no_user_created() {
		// when
		Optional<User> nonExistingId = tut.findBy(new UserId(5));
		Optional<User> nonExistingName = tut.findBy("Kamil");

		// then
		assertThat(nonExistingId.isEmpty()).isTrue();
		assertThat(nonExistingName.isEmpty()).isTrue();
	}

	@Test
	void should_find_by_username_added_users() {
		// given
		tut.create(new User(new Username("Dominik"), tut.nextUserId(), password));
		tut.create(new User(new Username("Patryk"), tut.nextUserId(), password));
		tut.create(new User(new Username("Michal"), tut.nextUserId(), password));

		// when
		Optional<User> dominik = tut.findBy("Dominik");
		Optional<User> patryk = tut.findBy("Patryk");
		Optional<User> michal = tut.findBy("Michal");
		List<User> users = List.of(dominik.get(), patryk.get(), michal.get());

		// then
		assertThat(users)
				.extracting(User::getUserId, User::getUserName)
				.contains(
						tuple(new UserId(1), new Username("Dominik")),
						tuple(new UserId(2), new Username("Patryk")),
						tuple(new UserId(3), new Username("Michal"))
				);
	}

	@Test
	void should_find_by_userid_added_users() {
		// given
		tut.create(new User(new Username("Dominik"), tut.nextUserId(), password));
		tut.create(new User(new Username("Patryk"), tut.nextUserId(), password));
		tut.create(new User(new Username("Michal"), tut.nextUserId(), password));

		// when
		Optional<User> dominik = tut.findBy(new UserId(1));
		Optional<User> patryk = tut.findBy(new UserId(2));
		Optional<User> michal = tut.findBy(new UserId(3));
		List<User> users = List.of(dominik.get(), patryk.get(), michal.get());

		// then
		assertThat(users)
				.extracting(User::getUserId, User::getUserName)
				.contains(
						tuple(new UserId(1), new Username("Dominik")),
						tuple(new UserId(2), new Username("Patryk")),
						tuple(new UserId(3), new Username("Michal"))
				);
	}

	@Test
	void next_id_should_be_one_higher_than_number_of_already_created_userIds() {
		// when
		tut.nextUserId();
		tut.nextUserId();
		tut.nextUserId();

		// then
		assertThat(tut.nextUserId().getValue()).isEqualTo(4);
	}

	@Test
	void should_throw_exception_when_creating_user_with_already_existing_userid() {
		// given
		tut.create(new User(new Username("Anna"), new UserId(1), password));

		// when
		Throwable thrown = catchThrowable(() -> tut.create(new User(new Username("Michal"), new UserId(1), password)));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}

	@Test
	void should_throw_exception_when_creating_user_with_already_existing_username() {
		// given
		tut.create(new User(new Username("Anna"), tut.nextUserId(), password));

		// when
		Throwable thrown = catchThrowable(() -> tut.create(new User(new Username("Anna"), tut.nextUserId(), password)));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

}
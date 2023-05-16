package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserSample;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

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
	void should_return_empty_when_no_user_found_in_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		final Optional<User> nonExistingId = tut.findBy(new UserId(5));
		final Optional<User> nonExistingName = tut.findBy("Kamil");

		// then
		assertThat(nonExistingId.isEmpty()).isTrue();
		assertThat(nonExistingName.isEmpty()).isTrue();
	}

	@Test
	void should_find_by_name_all_previously_written_to_file() {
		// given
		tut = new InFileUserRepository(existingUsersDbFilepath, existingUsersIdDbFilepath);

		// when
		final Optional<User> dominik = tut.findBy("Dominik");
		final Optional<User> patryk = tut.findBy("Patryk");
		final Optional<User> michal = tut.findBy("Michal");

		// then
		final List<User> users = List.of(dominik.get(), patryk.get(), michal.get());
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
		final Optional<User> dominik = tut.findBy(new UserId(1));
		final Optional<User> patryk = tut.findBy(new UserId(2));
		final Optional<User> michal = tut.findBy(new UserId(3));

		// then
		final List<User> users = List.of(dominik.get(), patryk.get(), michal.get());
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
		final UserId userId = tut.findNextUserId();

		// then
		assertThat(userId).isEqualTo(new UserId(4));
	}

	@Test
	void should_save_correctly_in_file() throws IOException {
		// given
		final File testUsersFile = new File(testFileUsers, "testUser.csv");
		tut = new InFileUserRepository(testUsersFile.getAbsolutePath(), testFileId.getAbsolutePath() + testFileId.getName());

		// when
		tut.create(UserSample.builder().username(new Username("Dominik")).userId(new UserId(1)).build());

		// then
		final String line = "Dominik,1";
		assertThat(Files.readAllLines(testUsersFile.toPath())).contains(line);
	}

	@Test
	void should_return_empty_when_no_user_created() {
		// when
		final Optional<User> nonExistingId = tut.findBy(createAnyUserId());
		final Optional<User> nonExistingName = tut.findBy(createAnyUsername().getValue());

		// then
		assertThat(nonExistingId.isEmpty()).isTrue();
		assertThat(nonExistingName.isEmpty()).isTrue();
	}

	@Test
	void should_find_by_username_added_users() {
		// given
		final UserId userOneId = createAnyUserId();
		final UserId userTwoId = createAnyUserId();
		final UserId userThreeId = createAnyUserId();
		final User userOne = UserSample.builder().userId(userOneId).build();
		final User userTwo = UserSample.builder().userId(userTwoId).build();
		final User userThree = UserSample.builder().userId(userThreeId).build();

		tut.create(userOne);
		tut.create(userTwo);
		tut.create(userThree);

		// when
		final Optional<User> optionalUserOne = tut.findBy(userOne.getUserName().getValue());
		final Optional<User> optionalUserTwo = tut.findBy(userTwo.getUserName().getValue());
		final Optional<User> optionalUserThree = tut.findBy(userThree.getUserName().getValue());

		// then
		final List<User> users = List.of(optionalUserOne.get(), optionalUserTwo.get(), optionalUserThree.get());
		assertThat(users)
				.extracting(User::getUserId, User::getUserName)
				.contains(
						tuple(userOneId, userOne.getUserName()),
						tuple(userTwoId, userTwo.getUserName()),
						tuple(userThreeId, userThree.getUserName())
				);
	}

	@Test
	void should_find_by_userid_added_users() {
		// given
		final UserId userOneId = createAnyUserId();
		final UserId userTwoId = createAnyUserId();
		final UserId userThreeId = createAnyUserId();
		final User userOne = UserSample.builder().userId(userOneId).build();
		final User userTwo = UserSample.builder().userId(userTwoId).build();
		final User userThree = UserSample.builder().userId(userThreeId).build();

		tut.create(userOne);
		tut.create(userTwo);
		tut.create(userThree);

		// when
		final Optional<User> optionalUserOne = tut.findBy(userOneId);
		final Optional<User> optionalUserTwo = tut.findBy(userTwoId);
		final Optional<User> optionalUserThree = tut.findBy(userThreeId);

		// then
		final List<User> users = List.of(optionalUserOne.get(), optionalUserTwo.get(), optionalUserThree.get());
		assertThat(users)
				.extracting(User::getUserId, User::getUserName)
				.contains(
						tuple(userOneId, userOne.getUserName()),
						tuple(userTwoId, userTwo.getUserName()),
						tuple(userThreeId, userThree.getUserName())
				);
	}

	@Test
	void next_userid_should_be_one_higher_than_previously_created_userid() {
		// given
		final UserId lastUserId = tut.nextUserId();

		// when
		final UserId nextUserId = tut.nextUserId();

		// then
		assertThat(nextUserId.getValue()).isEqualTo(lastUserId.getValue() + 1);
	}

	@Test
	void should_throw_exception_when_creating_user_with_already_existing_userid() {
		// given
		final UserId userId = createAnyUserId();
		tut.create(new User(createAnyUsername(), userId, createAnyUserPassword()));

		// when
		final Throwable thrown = catchThrowable(() -> tut.create(UserSample.builder().userId(userId).build()));

		// then
		assertThat(thrown).isInstanceOf(UserAlreadyExistsException.class);
	}

	@Test
	void should_throw_exception_when_creating_user_with_already_existing_username() {
		// given
		final Username createdUsername = createAnyUsername();
		tut.create(new User(createdUsername, createAnyUserId(), createAnyUserPassword()));

		// when
		final Throwable thrown = catchThrowable(() -> tut.create(UserSample.builder().username(createdUsername).build()));

		// then
		assertThat(thrown).isInstanceOf(UsernameIsTakenException.class);
	}

}
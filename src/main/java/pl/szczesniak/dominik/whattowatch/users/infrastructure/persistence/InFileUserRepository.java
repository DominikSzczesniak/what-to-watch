package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InFileUserRepository implements UserRepository {

	private final String fileNameOfUsers;
	private final String usersIdFileName;
	private final static int INDEX_WITH_USER_ID_NUMBER_IN_CSV = 1;
	private final static int INDEX_WITH_USERNAME_IN_CSV = 0;
	private final static int ID_OF_FIRST_CREATED_USER_EVER = 1;


	@Override
	public void create(final User user) {
		createFile();
		if (usernameIsTaken(user.getUserName().getValue())) {
			throw new UsernameIsTakenException("Please choose different name, " + user.getUserName() + " is already taken");
		}
		if (exists(user.getUserId())) {
			throw new UserAlreadyExistsException("user already exists");
		}
		try {
			final FileWriter fw = new FileWriter(fileNameOfUsers, true);
			final BufferedWriter bw = new BufferedWriter(fw);
			writeLine(user, bw);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	private boolean usernameIsTaken(final String username) {
		try (final BufferedReader br = new BufferedReader(new FileReader(fileNameOfUsers))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> stringLine = Arrays.stream(line.split("[,]")).toList();
				if (stringLine.get(INDEX_WITH_USERNAME_IN_CSV).equalsIgnoreCase(username)){
					return true;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return false;
	}

	private static void writeLine(final User user, final BufferedWriter bw) throws IOException {
		bw.write(user.getUserName().getValue() + "," + (user.getUserId().getValue()));
		bw.newLine();
		bw.close();
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		try (final BufferedReader br = new BufferedReader(new FileReader(fileNameOfUsers))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> stringLine = Arrays.stream(line.split("[,]")).toList();
				if (Integer.parseInt(stringLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) == (userId.getValue())) {
					return Optional.of(new User(new Username(stringLine.get(INDEX_WITH_USERNAME_IN_CSV)), new UserId(Integer.parseInt(stringLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)))));
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> findBy(final String username) {
		try (final BufferedReader br = new BufferedReader(new FileReader(fileNameOfUsers))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> stringLine = Arrays.stream(line.split("[,]")).toList();
				if (username.equals(stringLine.get(INDEX_WITH_USERNAME_IN_CSV))) {
					return Optional.of(new User(new Username(stringLine.get(INDEX_WITH_USERNAME_IN_CSV)), new UserId(Integer.parseInt(stringLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)))));
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return Optional.empty();
	}

	@Override
	public UserId nextUserId() {
		createFile();
		UserId userId = findNextUserId();
		overwriteUserIdFile(userId);
		return userId;
	}

	UserId findNextUserId() {
		int id = ID_OF_FIRST_CREATED_USER_EVER;
		try (final BufferedReader br = new BufferedReader(new FileReader(usersIdFileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				id = Integer.parseInt(line) + 1;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return new UserId(id);
	}

	private void overwriteUserIdFile(final UserId userId) {
		final String tempFile = "temp.csv";
		final File oldFile = new File(usersIdFileName);
		final File newFile = new File(tempFile);

		try {
			final FileWriter fw = new FileWriter(tempFile, true);
			final BufferedWriter bw = new BufferedWriter(fw);
			final PrintWriter pw = new PrintWriter(bw);
			final FileReader fr = new FileReader(usersIdFileName);
			final BufferedReader br = new BufferedReader(fr);

			pw.println(userId.getValue());

			closeAll(fw, bw, pw, fr, br);

			renameFile(oldFile, newFile);

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private void renameFile(final File oldFile, final File newFile) {
		oldFile.delete();
		final File dump = new File(usersIdFileName);
		newFile.renameTo(dump);
	}

	private static void closeAll(final FileWriter fw, final BufferedWriter bw, final PrintWriter pw,
								 final FileReader fr, final BufferedReader br) throws IOException {
		pw.flush();
		pw.close();
		fr.close();
		br.close();
		bw.close();
		fw.close();
	}

	@Override
	public boolean exists(final UserId userId) {
		return checkUserIdExist(userId);
	}

	private boolean checkUserIdExist(final UserId userId) {
		try (final BufferedReader br = new BufferedReader(new FileReader(fileNameOfUsers))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> listLine = Arrays.stream(line.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) == userId.getValue()) {
					return true;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return false;
	}

	private void createFile() {
		try {
			final File myObj = new File(fileNameOfUsers);
			final File myObjTwo = new File(usersIdFileName);
			if (myObj.createNewFile()) {
				System.out.println("Users file created: " + myObj.getName());
			}
			if (myObjTwo.createNewFile()) {
				System.out.println("Users id file created: " + myObjTwo.getName());

			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}


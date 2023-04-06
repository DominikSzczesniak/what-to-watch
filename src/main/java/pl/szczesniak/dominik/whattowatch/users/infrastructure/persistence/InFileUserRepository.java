package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class InFileUserRepository implements UserRepository {

	private final String fileName;
	private final static int INDEX_WITH_ID_NUMBER_IN_CSV = 1;
	private final static int INDEX_WITH_USERNAME_IN_CSV = 0;
	private final static int ID_OF_FIRST_CREATED_USER_EVER = 1;


	@Override
	public void saveUser(final User user) {
		createFile();
		if (exists(user.getUserId())) {
			throw new UserAlreadyExistsException("user already exists");
		}
		if (!userHasId(user.getUserName())) {
			try {
				final FileWriter fw = new FileWriter(fileName, true);
				final BufferedWriter bw = new BufferedWriter(fw);
				bw.write(user.getUserName() + "," + (user.getUserId().getValue()));
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	@Override
	public UserId nextUserId() {
		createFile();
		int id = ID_OF_FIRST_CREATED_USER_EVER;
		String lastLine = "";
		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				lastLine = line;
				final List<String> listLine = Arrays.stream(lastLine.split("[,]")).toList();
				id = Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)) + 1;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return new UserId(id);
	}

	@Override
	public boolean exists(final UserId userId) {
		return getExistingUserId(userId) != null;
	}

	@Override
	public List<User> findAll() {
		final List<User> listLine = new ArrayList<>();
		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> stringLine = Arrays.stream(line.split("[,]")).toList();
				listLine.add(new User(stringLine.get(0), new UserId(Integer.parseInt(stringLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)))));
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return listLine;
	}

	private void createFile() {
		try {
			final File myObj = new File(fileName);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

//	private boolean usernameIsTaken(final String userName) {
//		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				final List<String> listLine = Arrays.stream(line.split("[,]")).toList();
//				if (listLine.get(INDEX_WITH_USERNAME_IN_CSV).equals(userName)) {
//					return true;
//				}
//			}
//		} catch (IOException e) {
//			throw new UncheckedIOException(e);
//		}
//		return false;
//	}

	private UserId getExistingUserId(final UserId userId) {
		UserId id = null;
		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> listLine = Arrays.stream(line.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)) == userId.getValue()) {
					id = new UserId(Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)));
					return id;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return id;
	}

	private boolean userHasId(final String username) {
		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> listLine = Arrays.stream(line.split("[,]")).toList();
				if (listLine.get(INDEX_WITH_USERNAME_IN_CSV).equals(username)) {
					return true;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return false;
	}

}


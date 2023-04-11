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
import java.util.Optional;

@RequiredArgsConstructor
public class InFileUserRepository implements UserRepository {

	private final String fileName;
	private final static int INDEX_WITH_ID_NUMBER_IN_CSV = 1;
	private final static int INDEX_WITH_USERNAME_IN_CSV = 0;
	private final static int ID_OF_FIRST_CREATED_USER_EVER = 1;


	@Override
	public void create(final User user) {
		createFile();
		if (exists(user.getUserId())) {
			throw new UserAlreadyExistsException("user already exists");
		}
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

	@Override
	public UserId nextUserId() {
		createFile();
		int id = ID_OF_FIRST_CREATED_USER_EVER;
		id = findHighestUsedId(id); // TODO: optional
		return new UserId(id);
	}

	private int findHighestUsedId(int id) {
		String lastLine;
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
		return id;
	}

//	@Override
//	public List<User> findAll() {
//		final List<User> listLine = new ArrayList<>();
//		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				final List<String> stringLine = Arrays.stream(line.split("[,]")).toList();
//				listLine.add(new User(stringLine.get(0), new UserId(Integer.parseInt(stringLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)))));
//			}
//		} catch (IOException e) {
//			throw new UncheckedIOException(e);
//		}
//		return listLine;
//	}

	@Override
	public boolean exists(final UserId userId) {
		return getExistingUserId(userId) != null;
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		return Optional.empty();
	}

	@Override
	public Optional<User> findBy(final String username) {
		return Optional.empty();
	}

	private UserId getExistingUserId(final UserId userId) { // FIXME
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

}


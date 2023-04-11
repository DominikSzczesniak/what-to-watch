package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

import java.util.Objects;

@ToString
public class User {

	@Getter
	private final String userName; // TODO: klasa
	@Getter
	private final UserId userId;

	public User(final String userName, final UserId userId) {
		Objects.requireNonNull(userId, "UserId must not be null.");
		Objects.requireNonNull(userName, "Username must not be null.");
		this.userName = userName;
		if (!validateName(userName)) {
			throw new InvalidUsernameException("Invalid username: " + userName);
		}
		this.userId = userId;
	}

	public static boolean validateName(String name) {
		return name.matches("(?i)[a-z](.{0,23}[a-z])?");
	}

}

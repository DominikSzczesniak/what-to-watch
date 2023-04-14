package pl.szczesniak.dominik.whattowatch.users.domain.model;


import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

@Value
public class Username {

	String value;

	public Username(final String value) {
		this.value = value;
		if (value == null || !usernameContainsOnlyLetters(value) || value.length() <= 2) {
			throw new InvalidUsernameException("Invalid username: " + value + ". Username can have up to 25 characters and contain only letters.");
		}
	}

	public static boolean usernameContainsOnlyLetters(String name) {
		return name.matches("(?i)[a-z]([- ',.a-z]{0,23}[a-z])?");
	}

}

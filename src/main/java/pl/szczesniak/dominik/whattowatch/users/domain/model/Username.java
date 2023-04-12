package pl.szczesniak.dominik.whattowatch.users.domain.model;


import lombok.Getter;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

public class Username {

	@Getter
	private final String value;

	public Username(final String value) {
		this.value = value;
		if (!validateName(value)) {
			throw new InvalidUsernameException("Invalid username: " + value);
		}
	}

	public static boolean validateName(String name) {
		return name.matches("(?i)[a-z](.{0,23}[a-z])?");
	}

}

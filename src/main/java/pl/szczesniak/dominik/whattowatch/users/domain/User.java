package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Objects;

@ToString
public class User {

	@Getter
	private final Username userName;
	@Getter
	private final UserId userId;

	public User(final Username userName, final UserId userId) {
		Objects.requireNonNull(userId, "UserId must not be null.");
		Objects.requireNonNull(userName, "Username must not be null.");
		this.userName = userName;
		this.userId = userId;
	}

}

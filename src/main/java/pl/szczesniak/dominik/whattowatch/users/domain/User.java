package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static java.util.Objects.requireNonNull;

@ToString
public class User {

	@Getter
	private final Username userName;
	@Getter
	private final UserId userId;

	public User(final Username userName, final UserId userId) {
		this.userName = requireNonNull(userName, "Username must not be null.");
		this.userId = requireNonNull(userId, "UserId must not be null.");
	}

}

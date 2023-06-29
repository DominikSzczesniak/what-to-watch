package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository {

	private final Map<UserId, User> users = new HashMap<>();
	public final AtomicInteger nextId = new AtomicInteger(0);

	@Override
	public void create(final User user) {
		if (usernameIsTaken(user.getUsername().getValue())) {
			throw new UsernameIsTakenException("Please choose different name, " + user.getUsername() + " is already taken");
		}
		user.setUserId(nextId.incrementAndGet());
		users.put(user.getUserId(), user);
	}

	@Override
	public boolean exists(final UserId userId) {
		return users.containsKey(userId);
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		return Optional.ofNullable(users.get(userId));
	}

	@Override
	public Optional<User> findBy(final String username) {
		return users.values().stream().filter(user -> user.getUsername().getValue().equals(username)).findFirst();
	}

	private boolean usernameIsTaken(final String username) {
		return users.values().stream().anyMatch(user -> username.equalsIgnoreCase(user.getUsername().getValue()));
	}

}

package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {

	private final Map<UserId, User> users = new HashMap<>();
	public final AtomicLong nextId = new AtomicLong(0L);

	@Override
	public Long create(final User user) {
		if (usernameIsTaken(user.getUsername().getValue())) {
			throw new UsernameIsTakenException("Please choose different name, " + user.getUsername() + " is already taken");
		}
		long userId = nextId.incrementAndGet();
		users.put(new UserId(userId), user);
		return userId;
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

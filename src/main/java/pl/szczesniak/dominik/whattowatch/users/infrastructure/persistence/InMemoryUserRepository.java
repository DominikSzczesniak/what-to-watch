package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions.UserAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.exceptions.UsernameIsTakenException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository {

	private final Map<UserId, User> users = new HashMap<>();
	public final AtomicInteger nextId = new AtomicInteger(0);

	@Override
	public void create(final User user) {
		if (usernameIsTaken(user.getUserName().getValue())) {
			throw new UsernameIsTakenException("Please choose different name, " + user.getUserName() + " is already taken");
		}
		if (exists(user.getUserId())) {
			throw new UserAlreadyExistsException("user already exists");
		}
		users.put(user.getUserId(), user);
	}

	@Override
	public UserId nextUserId() {
		return new UserId(nextId.incrementAndGet());
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
		return users.values().stream().filter(user -> user.getUserName().getValue().equals(username)).findFirst();
	}

	private boolean usernameIsTaken(final String username) {
		return users.values().stream().anyMatch(user -> username.equalsIgnoreCase(user.getUserName().getValue()));
	}

}

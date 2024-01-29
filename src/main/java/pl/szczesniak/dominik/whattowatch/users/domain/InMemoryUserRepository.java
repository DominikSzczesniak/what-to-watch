package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.lang.reflect.Field;
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
		setId(user, nextId.incrementAndGet());
		users.put(user.getUserId(), user);
	}

	private void setId(final User user, final int id) {
		final Class<User> userClass = User.class;
		final Class<? super User> baseEntityClass = userClass.getSuperclass();
		try {
			final Field userId = baseEntityClass.getDeclaredField("id");
			userId.setAccessible(true);
			userId.set(user, id);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean exists(final UserId userId) {
		return users.containsKey(userId);
	}

	@Override
	public Optional<User> findBy(final Username username) {
		return users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		return users.values().stream().filter(user -> user.getUserId().equals(userId)).findFirst();
	}

	private boolean usernameIsTaken(final String username) {
		return users.values().stream().anyMatch(user -> username.equalsIgnoreCase(user.getUsername().getValue()));
	}

}

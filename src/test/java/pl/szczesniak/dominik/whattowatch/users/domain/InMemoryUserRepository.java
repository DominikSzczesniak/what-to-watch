package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;
import pl.szczesniak.dominik.whattowatch.users.query.UserQueryService;
import pl.szczesniak.dominik.whattowatch.users.query.model.UserQueryResult;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository, UserQueryService {

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
	public boolean isUsernameTaken(final Username username) {
		return users.values().stream().anyMatch(user -> user.getUsername().equals(username));
	}

	@Override
	public boolean exists(final UserId userId) {
		return users.containsKey(userId);
	}

	@Override
	public Optional<UserQueryResult> findUserQueryResult(final Username username) {
		return users.values().stream()
				.filter(user -> user.getUsername().equals(username)).findFirst()
				.map(user -> new UserQueryResult(
						user.getId(),
						user.getUsername().getValue(),
						user.getUserPassword().getValue(),
						toRoleNames(user.getRoles()))
				);
	}

	private static List<RoleName> toRoleNames(final List<UserRole> roles) {
		return roles.stream().map(UserRole::getRoleName).toList();
	}

	@Override
	public Optional<User> findBy(final Username username) {
		return users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
	}

	@Override
	public void deleteUser(final UserId userId) {
		users.remove(userId);
	}

	private boolean usernameIsTaken(final String username) {
		return users.values().stream().anyMatch(user -> username.equalsIgnoreCase(user.getUsername().getValue()));
	}

}

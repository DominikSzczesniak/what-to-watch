package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashSet;
import java.util.Set;

public class InMemoryUserProvider implements UserProvider {

	private final Set<UserId> existingUsers = new HashSet<>();

	public void addUser(UserId userId) {
		existingUsers.add(userId);
	}

	@Override
	public boolean exists(final UserId userId) {
		return existingUsers.contains(userId);
	}
}

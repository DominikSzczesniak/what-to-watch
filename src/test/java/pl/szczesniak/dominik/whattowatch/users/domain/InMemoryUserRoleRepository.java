package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryUserRoleRepository implements UserRoleRepository {

	private final HashMap<Integer, UserRole> roles = new HashMap<>();

	@Override
	public void create(final UserRole role) {
		roles.put(role.getId(), role);
	}

	@Override
	public Optional<UserRole> findBy(final RoleName roleName) {
		return roles.values().stream()
				.filter(role -> role.getRoleName().equals(roleName))
				.findFirst();
	}
}

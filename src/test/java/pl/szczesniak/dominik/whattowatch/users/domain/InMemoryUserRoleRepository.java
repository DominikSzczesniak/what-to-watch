package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryUserRoleRepository implements UserRoleRepository {

	private final HashMap<Integer, UserRole> roles = new HashMap<>();
	private final AtomicInteger nextId = new AtomicInteger(1);

	InMemoryUserRoleRepository() {
		create(List.of(RoleName.USER));
	}

	@Override
	public Optional<UserRole> findBy(final RoleName roleName) {
		return roles.values().stream()
				.filter(role -> role.getRoleName().equals(roleName))
				.findFirst();
	}

	@Override
	public void create(final List<RoleName> roles) {
		for (RoleName roleName : roles) {
			this.roles.put(nextId.getAndIncrement(), new UserRole(roleName));
		}
	}

}

package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

class InMemoryUserRoleRepository implements UserRoleRepository {

	private final HashMap<Integer, UserRole> roles = new HashMap<>();

	InMemoryUserRoleRepository() {
		addDefaultRoles(List.of(RoleName.USER));
	}

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

	@Override
	public void addDefaultRoles(final List<RoleName> roles) {
		int id = 1;
		for (RoleName roleName : roles) {
			this.roles.put(id++, new UserRole(roleName));
		}
	}

}

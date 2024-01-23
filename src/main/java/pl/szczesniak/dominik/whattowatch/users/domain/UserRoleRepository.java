package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.Optional;

public interface UserRoleRepository {

	void create(UserRole role);

	Optional<UserRole> findBy(RoleName roleName);

}

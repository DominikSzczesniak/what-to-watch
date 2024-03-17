package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {

	void create(List<RoleName> roles);

	Optional<UserRole> findBy(RoleName roleName);

}

@Repository
interface SpringDataJpaUserRoleRepository extends UserRoleRepository, JpaRepository<UserRole, Integer> {

	@Override
	default void create(List<RoleName> roles) {
		roles.forEach(roleName -> save(new UserRole(roleName)));
	}

	@Override
	default Optional<UserRole> findBy(RoleName roleName) {
		return findByRoleName(roleName);
	}

	Optional<UserRole> findByRoleName(final RoleName roleName);

}
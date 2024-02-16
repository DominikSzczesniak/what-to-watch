package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRole;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.Optional;

@Repository
public interface SpringDataJpaUserRoleRepository extends JpaRepository<UserRole, Integer> {

	Optional<UserRole> findByRoleName(final RoleName roleName);

}

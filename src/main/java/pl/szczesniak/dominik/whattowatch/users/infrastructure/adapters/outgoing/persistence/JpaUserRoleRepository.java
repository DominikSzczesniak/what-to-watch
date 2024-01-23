package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRole;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRoleRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRoleRepository implements UserRoleRepository {

	private final SpringDataJpaUserRoleRepository springDataJpaUserRoleRepository;

	@Override
	public void create(final UserRole role) {
		springDataJpaUserRoleRepository.save(role);
	}

	@Override
	public Optional<UserRole> findBy(final RoleName roleName) {
		return springDataJpaUserRoleRepository.findByRoleName(roleName);
	}
}

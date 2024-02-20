package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.List;
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

	@Override
	public void addDefaultRoles(final List<RoleName> roles) {
		final List<UserRole> userRolesInDatabase = springDataJpaUserRoleRepository.findAll();
		final List<RoleName> defaultRoles = List.of(RoleName.USER);

		final List<RoleName> rolesToAdd = defaultRoles.stream()
				.filter(roleName -> !roleExists(roleName, userRolesInDatabase))
				.toList();

		rolesToAdd.forEach(roleName -> springDataJpaUserRoleRepository.save(new UserRole(roleName)));
	}

	private boolean roleExists(final RoleName roleName, final List<UserRole> userRolesInDatabase) {
		return userRolesInDatabase.stream()
				.anyMatch(role -> role.getRoleName().equals(roleName));
	}

}

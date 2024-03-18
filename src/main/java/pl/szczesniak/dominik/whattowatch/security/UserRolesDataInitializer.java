package pl.szczesniak.dominik.whattowatch.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRoleRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
class UserRolesDataInitializer {

	private final UserRoleRepository roleRepository;

	@PostConstruct
	void createRoles() {
		final List<RoleName> roles = new ArrayList<>();
		roles.add(RoleName.USER);
		roleRepository.create(roles);
	}

}

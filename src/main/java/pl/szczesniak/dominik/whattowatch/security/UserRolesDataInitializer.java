package pl.szczesniak.dominik.whattowatch.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRoleRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.List;

@Component
@RequiredArgsConstructor
class UserRolesDataInitializer {

	private final UserRoleRepository roleRepository;

	@PostConstruct
	void addDefaultRoles() {
		roleRepository.addDefaultRoles(List.of(RoleName.USER));
	}

}

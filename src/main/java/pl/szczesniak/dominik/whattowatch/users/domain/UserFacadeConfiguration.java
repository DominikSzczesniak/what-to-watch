package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEventsPublisher;
import pl.szczesniak.dominik.whattowatch.users.query.UserQueryService;

@Configuration
class UserFacadeConfiguration {

	@Bean
	public UserFacade userFacade(final UserRepository userRepository,
								 final UserRoleRepository userRoleRepository,
								 final PasswordEncoder passwordEncoder,
								 final UserQueryService userQuerService,
								 final DomainEventsPublisher domainEventsPublisher) {
		return new UserFacade(
				new UserService(userRepository, userRoleRepository, passwordEncoder, domainEventsPublisher),
				userQuerService
		);
	}

}

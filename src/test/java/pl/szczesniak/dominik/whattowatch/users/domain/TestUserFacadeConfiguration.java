package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEventsPublisher;

public class TestUserFacadeConfiguration {

	static UserFacade userFacade(final DomainEventsPublisher eventPublisher) {
		final InMemoryUserRepository repository = new InMemoryUserRepository();
		return new UserFacade(
				new UserService(repository, new InMemoryUserRoleRepository(), new BCryptPasswordEncoder(), eventPublisher),
				repository
		);
	}

}



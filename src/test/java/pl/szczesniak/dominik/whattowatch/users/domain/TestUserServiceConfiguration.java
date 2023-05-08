package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.InMemoryUserRepository;

public class TestUserServiceConfiguration {

	static UserService userService() {
		return new UserService(new InMemoryUserRepository());
	}

}



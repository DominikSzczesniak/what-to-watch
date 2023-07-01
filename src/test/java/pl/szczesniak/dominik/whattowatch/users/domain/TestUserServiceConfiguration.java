package pl.szczesniak.dominik.whattowatch.users.domain;

public class TestUserServiceConfiguration {

	static UserService userService() {
		return new UserService(new InMemoryUserRepository());
	}

}



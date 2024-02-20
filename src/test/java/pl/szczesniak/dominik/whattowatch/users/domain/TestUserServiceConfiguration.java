package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestUserServiceConfiguration {

	static UserService userService() {
		return new UserService(new InMemoryUserRepository(), new InMemoryUserRoleRepository(), new BCryptPasswordEncoder());
	}

}



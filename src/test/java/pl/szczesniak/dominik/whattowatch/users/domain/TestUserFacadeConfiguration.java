package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestUserFacadeConfiguration {

	static UserFacade userFacade() {
		final InMemoryUserRepository repository = new InMemoryUserRepository();
		return new UserFacade(
				new UserService(repository, new InMemoryUserRoleRepository(), new BCryptPasswordEncoder()),
				repository
		);
	}

}



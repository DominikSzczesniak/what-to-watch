package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {

	@Bean
	public UserService userService(final UserRepository userRepository) {
		return new UserService(userRepository);
	}

}

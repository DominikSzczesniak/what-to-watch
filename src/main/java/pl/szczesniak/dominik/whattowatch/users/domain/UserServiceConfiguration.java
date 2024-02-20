package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserServiceConfiguration {

	@Bean
	public UserService userService(final UserRepository userRepository,
								   final UserRoleRepository userRoleRepository,
								   final PasswordEncoder passwordEncoder) {
		return new UserService(userRepository, userRoleRepository, passwordEncoder);
	}

}

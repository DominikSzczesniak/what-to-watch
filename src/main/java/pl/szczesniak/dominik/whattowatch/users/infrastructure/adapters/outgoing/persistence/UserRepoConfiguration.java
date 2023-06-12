package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.springframework.context.annotation.Bean;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;

//@Configuration
public class UserRepoConfiguration {

	@Bean
	public UserRepository userRepository() {
		return new InMemoryUserRepository();
	}

}

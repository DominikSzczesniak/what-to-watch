package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;

@Configuration
public class MemoryProfileUserRepoConfiguration {

	@Bean
	public UserRepository userRepository() {
		return new InMemoryUserRepository();
	}

}

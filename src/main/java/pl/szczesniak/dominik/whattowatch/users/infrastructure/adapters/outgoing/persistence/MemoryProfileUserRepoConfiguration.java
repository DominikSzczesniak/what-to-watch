package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;

@Profile("memory")
@Configuration
public class MemoryProfileUserRepoConfiguration {

	@Bean
	public UserRepository userRepository() {
		return new InMemoryUserRepository();
	}

}

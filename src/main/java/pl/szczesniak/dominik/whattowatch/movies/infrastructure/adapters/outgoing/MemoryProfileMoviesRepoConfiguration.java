package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;

@Profile("memory")
@Configuration
public class MemoryProfileMoviesRepoConfiguration {

	@Bean
	public MoviesRepository moviesRepository() {
		return new InMemoryMoviesRepository();
	}

}

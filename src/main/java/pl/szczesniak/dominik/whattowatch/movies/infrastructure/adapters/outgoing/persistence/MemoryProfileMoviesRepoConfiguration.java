package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;

@Configuration
public class MemoryProfileMoviesRepoConfiguration {

	@Bean
	public MoviesRepository moviesRepository() {
		return new InMemoryMoviesRepository();
	}

}

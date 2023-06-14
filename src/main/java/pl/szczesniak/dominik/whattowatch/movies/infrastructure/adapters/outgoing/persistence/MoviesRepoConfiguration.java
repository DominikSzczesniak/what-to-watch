package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;

//@Configuration
public class MoviesRepoConfiguration {

//	@Bean
	public MoviesRepository moviesRepository() {
		return new InMemoryMoviesRepository();
	}

}

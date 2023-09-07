package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaMoviesRepository extends JpaRepository<Movie, Integer> {

	List<Movie> findAllByUserId(UserId userId);

	Optional<Movie> findByMovieIdAndUserId(Integer movieId, UserId userId);

}

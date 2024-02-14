package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Repository
public interface SpringDataJpaMoviesRepository extends JpaRepository<Movie, Integer> {

	Optional<Movie> findByIdAndUserId(Integer movieId, UserId userId);

}

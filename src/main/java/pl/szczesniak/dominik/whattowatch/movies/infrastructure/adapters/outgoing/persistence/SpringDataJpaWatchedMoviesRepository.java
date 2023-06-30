package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.WatchedMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@Repository
public interface SpringDataJpaWatchedMoviesRepository extends JpaRepository<WatchedMovie, MovieId> {

	List<WatchedMovie> findAllByUserId(UserId userId);

}

package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;

@Repository
public interface SpringDataJpaWatchedMoviesRepository extends JpaRepository<WatchedMovie, MovieId> {

}

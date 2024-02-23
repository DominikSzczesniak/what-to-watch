package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;

@Repository
public interface SpringDataJpaMovieTagsRepository extends JpaRepository<MovieTag, MovieTagId> {

}

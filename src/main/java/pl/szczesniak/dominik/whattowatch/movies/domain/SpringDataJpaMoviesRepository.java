package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaMoviesRepository extends JpaRepository<Movie, Integer> {

	List<Movie> findAllByUserId(UserId userId);

	Optional<Movie> findByIdAndUserId(Integer movieId, UserId userId);

	@Query("SELECT m FROM Movie m JOIN m.tags t WHERE t.tagId IN :tagIds AND m.userId = :userId GROUP BY m HAVING COUNT(DISTINCT t.tagId) = :#{#tagIds.size()}")
	List<Movie> findAllByTags_TagIdInAndUserId(@Param("tagIds") List<MovieTagId> tagIds, @Param("userId") UserId userId);
}

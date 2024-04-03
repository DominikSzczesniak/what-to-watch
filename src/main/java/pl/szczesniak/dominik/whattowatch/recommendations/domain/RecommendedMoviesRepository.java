package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;

interface RecommendedMoviesRepository {

	RecommendedMoviesId create(RecommendedMovies recommendedMovies);

}

@Repository
interface SpringDataJpaRecommendedMoviesRepository extends RecommendedMoviesRepository, JpaRepository<RecommendedMovies, Long> {

	@Override
	default RecommendedMoviesId create(RecommendedMovies recommendedMovies) {
		final RecommendedMovies recommendation = save(recommendedMovies);
		return recommendation.getRecommendedMoviesId();
	}

}
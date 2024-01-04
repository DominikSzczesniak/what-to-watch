package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendedMovies;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendedMoviesRepository;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendedMoviesId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
class JpaRecommendedMoviesRepository implements RecommendedMoviesRepository {

	private final SpringDataJpaRecommendedMoviesRepository springDataJpaRecommendedMoviesRepository;

	@Override
	public RecommendedMoviesId create(final RecommendedMovies recommendedMovies) {
		final RecommendedMovies recommendation = springDataJpaRecommendedMoviesRepository.save(recommendedMovies);
		return recommendation.getRecommendedMoviesId();
	}

	@Override
	public Optional<RecommendedMovies> findLatestRecommendedMovies(final UserId userId) {
		return Optional.ofNullable(springDataJpaRecommendedMoviesRepository.findTopByUserIdOrderByCreationDateDesc(userId));
	}

	@Override
	public boolean existsByUserIdAndRecommendationDateBetween(final UserId userId,
															  final LocalDateTime intervalStart,
															  final LocalDateTime intervalEnd) {
		return springDataJpaRecommendedMoviesRepository.existsByUserIdAndStartDateAndEndInterval(userId, intervalStart, intervalEnd);
	}

}

package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

interface RecommendationsRepository {

	Optional<UserMoviesRecommendations> findBy(UserId userId);

	void create(UserMoviesRecommendations userMoviesRecommendations);

}

@Repository
interface SpringDataJpaAggregateRepository extends RecommendationsRepository, JpaRepository<UserMoviesRecommendations, UserId> {


	@Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	@Override
	default Optional<UserMoviesRecommendations> findBy(UserId userId) {
		return findByUserId(userId);
	}

	@Override
	default void create(UserMoviesRecommendations userMoviesRecommendations) {
		save(userMoviesRecommendations);
	}

	Optional<UserMoviesRecommendations> findByUserId(final UserId userId);

}

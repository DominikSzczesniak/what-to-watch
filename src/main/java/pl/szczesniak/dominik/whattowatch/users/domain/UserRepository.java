package pl.szczesniak.dominik.whattowatch.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Optional;

interface UserRepository {

	void create(User user);

	boolean exists(UserId userId);

	Optional<User> findBy(Username username);

	void deleteUser(UserId userId);
}

@Repository
interface SpringDataJpaUserRepository extends UserRepository, JpaRepository<User, Integer> {

	@Override
	default void deleteUser(UserId userId) {
		deleteById(userId.getValue());
	}

	@Override
	default void create(User user) {
		save(user);
	}

	@Override
	default boolean exists(UserId userId) {
		return existsById(userId.getValue());
	}

	@Override
	default Optional<User> findBy(Username username) {
		return findUserByUsername(username);
	}

	Optional<User> findUserByUsername(Username username);

}
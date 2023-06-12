package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

	private final SpringDataJpaRepository springDataJpaRepository;
	public final AtomicInteger nextId = new AtomicInteger(0);

	@Override
	public void create(final User user) {
		springDataJpaRepository.save(user);
	}

	@Override
	public UserId nextUserId() {
		return new UserId(nextId.incrementAndGet());
	}

	@Override
	public boolean exists(final UserId userId) {
		return springDataJpaRepository.existsById(userId.getValue());
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		return springDataJpaRepository.findById(userId.getValue());
	}

	@Override
	public Optional<User> findBy(final String username) {
		return springDataJpaRepository.findByUserNameEquals(username);
	}

}

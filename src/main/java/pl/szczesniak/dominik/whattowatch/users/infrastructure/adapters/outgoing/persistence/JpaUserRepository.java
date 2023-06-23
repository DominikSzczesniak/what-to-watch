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

	public final AtomicInteger nextId = new AtomicInteger(0);

	private final SpringDataJpaUserRepository springDataJpaUserRepository;

	@Override
	public void create(final User user) {
		springDataJpaUserRepository.save(user);
	}

	@Override
	public UserId nextUserId() {
		return new UserId(nextId.incrementAndGet());
	}

	@Override
	public boolean exists(final UserId userId) {
		return springDataJpaUserRepository.existsById(userId);
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		return springDataJpaUserRepository.findById(userId);
	}

	@Override
	public Optional<User> findBy(final String username) {
		return springDataJpaUserRepository.findUserByUsername_Value(username);
	}

}

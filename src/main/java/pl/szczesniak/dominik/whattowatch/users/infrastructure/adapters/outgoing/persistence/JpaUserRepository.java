package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

	private final SpringDataJpaUserRepository springDataJpaUserRepository;
	public final AtomicInteger nextId = new AtomicInteger(0);

	@Override
	public void create(final User user) {
		if (usernameIsTaken(user.getUsername())) {
			throw new UsernameIsTakenException("Please choose different name, " + user.getUsername() + " is already taken");
		}
		springDataJpaUserRepository.save(user);
	}

	private boolean usernameIsTaken(final Username username) {
		List<User> all = springDataJpaUserRepository.findAll();
		return all.stream().anyMatch(user -> user.getUsername().getValue().equalsIgnoreCase(username.getValue()));
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
		return springDataJpaRepository.findUserByUsername_Value(username);
	}

}

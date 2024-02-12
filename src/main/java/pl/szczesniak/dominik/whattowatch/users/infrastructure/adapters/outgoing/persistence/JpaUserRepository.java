package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

	private final SpringDataJpaUserRepository springDataJpaUserRepository;

	@Override
	public void create(final User user) {
		springDataJpaUserRepository.save(user);
	}

	@Override
	public boolean exists(final UserId userId) {
		return springDataJpaUserRepository.existsById(userId.getValue());
	}

	@Override
	public Optional<User> findBy(final Username username) {
		return springDataJpaUserRepository.findUserByUsername(username);
	}

}

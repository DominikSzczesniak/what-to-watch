package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {


	private final SpringDataJpaUserRepository springDataJpaUserRepository;

	@Override
	public Long create(final User user) {
		final User savedUser = springDataJpaUserRepository.save(user);
		return user.getUserId();
	}


	@Override
	public boolean exists(final UserId userId) {
		return springDataJpaUserRepository.existsById(userId.getValue());
	}

	@Override
	public Optional<User> findBy(final UserId userId) {
		return springDataJpaUserRepository.findById(userId.getValue());
	}

	@Override
	public Optional<User> findBy(final String username) {
		return springDataJpaUserRepository.findUserByUsername_Value(username);
	}

}

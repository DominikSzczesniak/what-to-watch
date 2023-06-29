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
	public void create(final User user) {
		springDataJpaUserRepository.save(user);
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

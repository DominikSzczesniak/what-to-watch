package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public Optional<User> findBy(final String username) {
		return springDataJpaUserRepository.findUserByUsername_Value(username);
	}

	@Override
	public List<UserId> findAllUsers() {
		return springDataJpaUserRepository.findAll()
				.stream()
				.map(User::getUserId)
				.collect(Collectors.toList());
	}

}

package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserNotFoundException;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	public UserId createUser(final String username) {
		User user = new User(new Username(username), repository.nextUserId());
		repository.create(user);
		return user.getUserId();
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}

	public User findBy(final UserId userId) {
		if (repository.findBy(userId).isPresent()) {
			return repository.findBy(userId).get();
		} else {
			throw new UserNotFoundException("User with userId: " + userId + " was not found.");
		}
	}

	public User findBy(final String username) {
		if (repository.findBy(username).isPresent()) {
			return repository.findBy(username).get();
		} else {
			throw new UserNotFoundException("User with username: " + username + " was not found.");
		}
	}

	public UserId login(String username) {
		return findBy(username).getUserId();
	}

}

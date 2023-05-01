package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	public UserId createUser(final Username username, final UserPassword userPassword) {
		final User user = new User(username, repository.nextUserId(), userPassword);
		repository.create(user);
		return user.getUserId();
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}

	public UserId login(final Username username, final UserPassword userPassword) {
		return Optional.ofNullable(repository
				.findBy(username.getValue())
				.filter(user -> user.getUserPassword().equals(userPassword))
				.orElseThrow(() -> new InvalidCredentialsException("User with username: " + username.getValue() + " not found"))).get().getUserId();
	}

}

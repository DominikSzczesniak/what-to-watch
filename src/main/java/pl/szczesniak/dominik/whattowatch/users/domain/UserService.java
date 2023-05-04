package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	public UserId createUser(final CreateUser command) {
		final User user = new User(command.getUsername(), repository.nextUserId(), command.getUserPassword());
		repository.create(user);
		return user.getUserId();
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}

	public UserId login(final Username username, final UserPassword userPassword) {
		return repository.findBy(username.getValue())
				.filter(user -> user.getUserPassword().equals(userPassword))
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials, could not log in.")).getUserId();
	}

}

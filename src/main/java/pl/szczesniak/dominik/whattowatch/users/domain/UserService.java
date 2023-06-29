package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	public Long createUser(final CreateUser command) {
		final User user = new User(command.getUsername(), command.getUserPassword());
		final Long createdUserId = repository.create(user);
		user.setUserId(new UserId(createdUserId));
		return createdUserId;
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}

	public Long login(final Username username, final UserPassword userPassword) {
		return repository.findBy(username.getValue())
				.filter(user -> user.getUserPassword().equals(userPassword))
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials, could not log in.")).getUserId();
	}

	public boolean isUsernameTaken(final Username username) {
		return repository.findBy(username.getValue()).isPresent();
	}

}
